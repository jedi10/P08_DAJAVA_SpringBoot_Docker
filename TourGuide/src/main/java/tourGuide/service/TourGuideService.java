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
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;
	private ExecutorService executorService;

	public static int incr = 0;
	public static final AtomicInteger incrementalCounter = new AtomicInteger(0);

	public void shutDownExecutorService() throws InterruptedException {
		if (executorService.isTerminated()){
			executorService.shutdown();
		}
		//executorService.shutdown();
		//executorService.awaitTermination(15, TimeUnit.MINUTES);
	}
	
	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
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
	
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(), 
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/**
	 * Tested by TestPerformance
	 * @param userList userList is mandatory
	 */
	public void trackUserLocationPerformance(List<User> userList){

		for(User user : userList) {
			Runnable runnableTask = () -> trackUserLocation(user);
			//Future<VisitedLocation> futureVisitedService =
			executorService.submit(()->	runnableTask);
			//System.out.println("Passed for "+ user.getUserName());
			/*VisitedLocation visitedLocation = null;
			try {
				visitedLocation = futureVisitedService.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} finally {
			}*/

			/*VisitedLocation visitedLocation = null;
		executorService.execute(new Runnable() {
			User user;
			TourGuideService userService;

			public void run() {
				this.userService.trackUserLocation(this.user);
				System.out.println("ok passed");
			}

			public Runnable init(TourGuideService userService, User user) {
				this.userService = userService;
				this.user = user;
				return this;
			}
		}.init(this, user));*/
		}
		try {
			shutDownExecutorService();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public VisitedLocation trackUserLocation(User user) {
		//Task1
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

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for(Attraction attraction : gpsUtil.getAttractions()) {
			if(rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}
		
		return nearbyAttractions;
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