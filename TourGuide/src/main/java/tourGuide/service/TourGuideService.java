package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.TourGuideController;
import tourGuide.domain.NearByAttraction;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.web.dto.NearByUserAttractionDTO;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	private final RewardCentral rewardCentral;
	public final Tracker tracker;
	boolean testMode = true;
	private ExecutorService executorService;

	public static int incr = 0;
	public static final AtomicInteger incrementalCounter = new AtomicInteger(0);

	public void shutDownExecutorService() {

		executorService.shutdown();//https://howtodoinjava.com/java/multi-threading/executorservice-shutdown/
		try {
			boolean executorHasFinished = executorService.awaitTermination(15, TimeUnit.MINUTES);
			if (!executorHasFinished) {
				logger.error("trackUserLocationforUserList does not finish in 15 minutes elapsed time");
				executorService.shutdownNow();
			} else {
				logger.debug("trackUserLocationforUserList finished before the 15 minutes elapsed time");
			}
		} catch (InterruptedException interruptedException) {
			logger.error("executorService was Interrupted : " + interruptedException.getLocalizedMessage());
			executorService.shutdownNow();
		}
	}
	
	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardCentral = rewardCentral;
		this.rewardsService = rewardsService;
		this.executorService = Executors.newFixedThreadPool(1500);//.newCachedThreadPool()
		Locale.setDefault(new Locale("en", "US"));
		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}
	
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}
	
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}
	
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * <b>Get Trip Deals personalised with user Preferences</b>
	 * <p>used by /getTripDeals POST EndPoint </p>
	 * @see tourGuide.TourGuideController#getTripDeals(String)
	 * @param user mandatory
	 * @return ist of providers
	 */
	public List<Provider> getTripDeals(User user) {
		List<Provider> resultList = new ArrayList<>();
		int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(), 
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulativeRewardPoints);
		user.setTripDeals(providers);

		//filtering providers with user price preference
		double priceLow = user.getUserPreferences().getLowerPricePoint().getNumber().doubleValue();
		double priceHigh = user.getUserPreferences().getHighPricePoint().getNumber().doubleValue();

		resultList = providers.stream().filter(p -> priceLow < p.price && p.price < priceHigh)
				.collect(Collectors.toList());

		return resultList;
	}

	/**
	 * Tested by TestPerformance
	 * @param userList userList is mandatory
	 */
	public void trackUserLocationUserList(List<User> userList){

		userList.parallelStream().forEach(
				(user) -> {
					Runnable runnableTask = () -> trackUserLocation(user);
					//Future<VisitedLocation> futureVisitedService =
					executorService.submit(()->	runnableTask);
			/*VisitedLocation visitedLocation = null;
			try {
				visitedLocation = futureVisitedService.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} finally {
			}*/
				}
		);
		shutDownExecutorService();
	}

	public VisitedLocation trackUserLocation(User user) {
		//Task1 - random generator for longitude and latitude with TreadLocalRandom https://www.codeflow.site/fr/article/java-thread-local-random
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		//Task2 - need visitedLocation to be created
		user.addToVisitedLocations(visitedLocation);
		//Task3 - need visited location to be referenced
		rewardsService.calculateRewards(user);

		//incr+=1;
		//System.out.println(incr+" : Passed for: "+ user.getUserName());
		incrementalCounter.addAndGet(1);
		//System.out.println(incrementalCounter.get()+" : Passed for: "+ user.getUserName());
		return visitedLocation;
	}

	/**
	 * <b>return the 5 closest attractions based from user's actual location</b>
	 * <p>5 user nearest attractions from the last user localisation without any distance limit</p>
	 * <p>Used by /getNearbyAttractions (POST) endpoint
	 * @see tourGuide.TourGuideController#getNearbyAttractions(String)
	 * @param user mandatory
	 * @return NearByUserAttractionDTO
	 */
	public NearByUserAttractionDTO getNearByAttractions(User user) {
		VisitedLocation visitedLocation = this.getUserLocation(user);
		NearByUserAttractionDTO result;
		List<NearByAttraction> nearByAttractions = new ArrayList<>();

		List<NearByAttraction> nearByAttractionsSorted;

		for(Attraction attraction : gpsUtil.getAttractions()) {
			Double distance = rewardsService.getDistance(
					new Location(attraction.longitude, attraction.latitude),
					visitedLocation.location);
			nearByAttractions.add(new NearByAttraction(attraction, distance));
		}
		nearByAttractionsSorted = nearByAttractions.stream()
				.sorted(Comparator.comparingDouble(NearByAttraction::getDistance))//.reversed())
				.limit(5)
				.collect(Collectors.toList());

		result = new NearByUserAttractionDTO(user, visitedLocation, nearByAttractionsSorted, rewardCentral);
		
		return result;
	}

	/**
	 * <b>Get all users actual locations</b>
	 * <p>Used by /getAllCurrentLocations (GET) endpoint</p>
	 * @see TourGuideController#getAllCurrentLocations()
	 * @return Map of Users Location (Value) with user UUID (Key)
	 */
	public Map<String, Location> getAllCurrentLocations(){
		Map<String, Location> allCurrentLocationsMap = new HashMap<>();
		this.getAllUsers().forEach(user ->
				allCurrentLocationsMap.put(user.getUserId().toString(), user.getLastVisitedLocation().location));
		return allCurrentLocationsMap;
	}
	
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}
	
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
	
}



//https://techvidvan.com/tutorials/java-executorservice/
//https://www.baeldung.com/java-executor-service-tutorial
//https://openclassrooms.com/fr/courses/5684021-scale-up-your-code-with-java-concurrency/6542491-write-concurrent-applications-using-thread-pools-and-futures