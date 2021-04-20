package tourGuide;

import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
//import org.junit.Ignore;
//import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPerformance {

	private ExecutorService executorService;
	
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */

	@BeforeAll
	public void beforeAll(){
		//this.executorService = Executors.newFixedThreadPool(100);
		Locale.setDefault(new Locale("en", "US"));
    }

    @AfterAll
	public void afterAll(){
		//this.executorService.shutdown();
	}

	@Order(1)
	@Test
	public void gpsUtil_getUserLocation_parseDouble(){
		double numberDecimal = -162.7119212345678;
		String number =  String.format("%.6f", numberDecimal);
		//Java uses my Locale's decimal separator (a comma) while I would like to use a point
		//https://stackoverflow.com/questions/5236056/force-point-as-decimal-separator-in-java
		//String number2 = "-162,711921";
		//https://mkyong.com/java/java-convert-string-to-double/
		//number2 = Double.parseDouble(number2.replace(",", "."));
		Double.parseDouble(number);
	}
	
	//@Disabled
	@Order(2)
	@DisplayName("Track Location")
	@ParameterizedTest(name = "For {0} User(s)")
	@CsvSource({"100"})
	public void highVolumeTrackLocation(int userNumber) {
		this.executorService = Executors.newFixedThreadPool(userNumber);
		Locale.setDefault(new Locale("en", "US"));
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(userNumber);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		List<User> finalAllUsers = allUsers;
		int midIndex = (((allUsers.size()) / 2) - 1);
		List<List<User> > lists = new ArrayList<>(
				allUsers.stream()
						.collect(Collectors.partitioningBy(s -> finalAllUsers.indexOf(s) > midIndex))  //https://www.geeksforgeeks.org/split-a-list-into-two-halves-in-java/
						.values());


		List<User> userList1 = lists.get(0);
		List<User> userList2 = lists.get(1);
		
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		//List<VisitedLocation> visitedLocationList = new ArrayList<>();
		List<String> passedUserList = new ArrayList<>();
		for(int i = 0; i<userNumber/2 ; i++ ) {
			int counterNumber = i;
			//Future<VisitedLocation> futureVisitedService1 =
					executorService.submit(()-> {
						//System.out.println("Passed for "+ userList1.get(counterNumber).getUserName());
						return tourGuideService.trackUserLocation(userList1.get(counterNumber));
					}
			);
			//Future<VisitedLocation> futureVisitedService2 =
					executorService.submit(()-> {
						//System.out.println("Passed for "+ userList2.get(counterNumber).getUserName());
						return tourGuideService.trackUserLocation(userList2.get(counterNumber));
					}
			);
			passedUserList.add(userList1.get(counterNumber).getUserName());
			passedUserList.add(userList2.get(counterNumber).getUserName());
			/*VisitedLocation visitedLocation = null;
			try {
				visitedLocationList.add(futureVisitedService1.get());
				visitedLocationList.add(futureVisitedService2.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} finally {
				assertTrue(visitedLocationList.size()== 100);
			}*/
		}
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();
		/*
		try {
			this.executorService.awaitTermination(15, TimeUnit.MINUTES);
			//tourGuideService.shutDownExecutorService();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(passedUserList.size() == userNumber);
		this.executorService.shutdown();
	}
	
	@Disabled
	@Order(3)
	@Test
	public void highVolumeGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
	    Attraction attraction = gpsUtil.getAttractions().get(0);
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

	    allUsers.forEach(u -> rewardsService.calculateRewards(u));

		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	
}



//https://techvidvan.com/tutorials/java-executorservice/