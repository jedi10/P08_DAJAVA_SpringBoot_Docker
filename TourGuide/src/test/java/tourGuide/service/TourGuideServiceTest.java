package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import rewardCentral.RewardCentral;
import tourGuide.domain.User;
import tourGuide.helper.InternalTestHelper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TourGuideServiceTest {

    @Test
    void getAllCurrentLocations() {
        //Given
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
        VisitedLocation visitedLocation2 = tourGuideService.trackUserLocation(user2);
        tourGuideService.tracker.stopTracking();

        List<User> allUsers = tourGuideService.getAllUsers();
        assertTrue(allUsers.size() == 2);
        assertNotNull(visitedLocation);
        assertNotNull(visitedLocation2);

        //When
        Map<String, Location> currentLocationsMap = tourGuideService.getAllCurrentLocations();

        //Then
        assertNotNull(currentLocationsMap);
        assertEquals(2, currentLocationsMap.size());
        assertEquals(visitedLocation.location, currentLocationsMap.get(user.getUserId().toString()));
        assertEquals(visitedLocation2.location, currentLocationsMap.get(user2.getUserId().toString()));
    }
}