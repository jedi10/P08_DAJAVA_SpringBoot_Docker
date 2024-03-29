package tourGuide;

import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.StopWatch;
//import org.junit.Ignore;
//import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.configuration.MicroserviceProperties;
import tourGuide.service.restTemplateService.GpsUtilRestService;
import tourGuide.tool.GpsUtilLocal;
import tourGuide.tool.ListTools;
import tourGuide.domain.Attraction;
import tourGuide.domain.Location;
import tourGuide.domain.VisitedLocation;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.domain.User;

/**
 * <b>Important: Need Microservice GpsUtil</b>
 * <p>Docker has to be active to load in container GpsUtil Microservice Project</p>
 * <p>You can pass over the docker resource by using the application in local mode: turn MicroserviceProperties.dockerActive to false</p>
 * @see tourGuide.configuration.MicroserviceProperties
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPerformance {

	private Logger logger = LoggerFactory.getLogger(TestPerformance.class);

	private ExecutorService executorService;

	@Autowired
	private GpsUtilRestService gpsUtilRestService;
	
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
		this.executorService = Executors.newFixedThreadPool(1500);
		Locale.setDefault(new Locale("en", "US"));
    }

    @AfterAll
	public void afterAll(){
		this.executorService.shutdown();
		/*try {
			boolean executorHasFinished = executorService.awaitTermination(20, TimeUnit.MINUTES);
			if (!executorHasFinished) {
				logger.error("Test does not finish in 15 minutes elapsed time");
				executorService.shutdownNow();
			} else {
				logger.debug("Test finished before the 15 minutes elapsed time");
			}
		} catch (InterruptedException interruptedException) {
			logger.error("Test was Interrupted : " + interruptedException.getLocalizedMessage());
			executorService.shutdownNow();
		}*/
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
	@CsvSource({"1000"})//,"1000","5000","10000","50000","100000"})
	public void highVolumeTrackLocation(int userNumber) {
		GpsUtilLocal gpsUtil = new GpsUtilLocal();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, gpsUtilRestService, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(userNumber);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, gpsUtilRestService, rewardsService, rewardCentral);

		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		tourGuideService.trackUserLocationUserList(allUsers);
		//for(User user : allUsers) {
		//	tourGuideService.trackUserLocation(user);
		//}
		AtomicInteger incrementalUser = TourGuideService.incrementalCounter;
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		//System.out.println("Users Number processed: "+ incrementalUser.toString());
	}
	
	//@Disabled
	@Order(3)
	@DisplayName("Track Reward")
	@ParameterizedTest(name = "For {0} User(s)")
	@CsvSource({"1000"})//,"1000","5000","10000","50000","100000"})
	public void highVolumeGetRewards(int userNumber) {
		//GIVEN
		GpsUtilLocal gpsUtil = new GpsUtilLocal();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, gpsUtilRestService, new RewardCentral());

		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(userNumber);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, gpsUtilRestService, rewardsService, rewardCentral);
		
	    Attraction attraction = ListTools.getAttractions().get(0);
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		//WHEN
		allUsers.parallelStream().forEach(
				(user) -> {
					Runnable runnableTask = () -> {
						user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(attraction.longitude, attraction.latitude), new Date()));
						rewardsService.calculateRewards(user);
						assertTrue(user.getUserRewards().size() > 0);
						System.out.println("ok passed");
					};
					//Future<VisitedLocation> futureVisitedService =
					executorService.submit(()->	runnableTask);
				});
		//THEN
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
}



//https://techvidvan.com/tutorials/java-executorservice/