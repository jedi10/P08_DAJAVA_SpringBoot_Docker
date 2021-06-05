package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.service.restTemplateService.GpsUtilRestService;
import tourGuide.tool.GpsUtilLocal;
import tourGuide.tool.ListTools;
import tourGuide.domain.Attraction;
import tourGuide.domain.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;

@SpringBootTest
public class TestRewardsService {

	@Autowired
	private GpsUtilRestService gpsUtilRestService;

	@Test
	public void userGetRewards() {
		GpsUtilLocal gpsUtil = new GpsUtilLocal();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, gpsUtilRestService, rewardsService, rewardCentral);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = ListTools.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		GpsUtilLocal gpsUtil = new GpsUtilLocal();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		Attraction attraction = ListTools.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	//@Ignore
	@Test
	public void nearAllAttractions() {
		GpsUtilLocal gpsUtil = new GpsUtilLocal();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, gpsUtilRestService, rewardsService, rewardCentral);
		User userGiven = tourGuideService.getAllUsers().get(0);
		tourGuideService.tracker.stopTracking();
		//WHEN
		rewardsService.calculateRewards(userGiven);//can throw ConcurrentModificationException Fixed with CopyOnWriteArrayList
		List<UserReward> userRewards = tourGuideService.getUserRewards(userGiven);
		//THEN

		assertEquals(tourGuide.tool.ListTools.getAttractions().size(), userRewards.size());
	}
	
}
