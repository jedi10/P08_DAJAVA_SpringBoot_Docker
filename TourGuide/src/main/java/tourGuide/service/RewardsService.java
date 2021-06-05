package tourGuide.service;

import org.springframework.stereotype.Service;

import tourGuide.configuration.MicroserviceProperties;
import tourGuide.service.restTemplateService.GpsUtilRestService;
import tourGuide.tool.GpsUtilLocal;
import tourGuide.tool.ListTools;
import tourGuide.domain.Attraction;
import tourGuide.domain.Location;
import tourGuide.domain.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	MicroserviceProperties microserviceProperties;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtilLocal gpsUtil;
	private final GpsUtilRestService gpsUtilRestService;
	private final RewardCentral rewardsCentral;
	
	public RewardsService(GpsUtilLocal gpsUtil, GpsUtilRestService gpsUtilRestService,
						  RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.gpsUtilRestService = gpsUtilRestService;
		this.rewardsCentral = rewardCentral;
		microserviceProperties = new MicroserviceProperties();
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}
	
	public void calculateRewards(User user) {
		//to avoid throw ConcurrentModificationException https://devstory.net/13641/java-copyonwritearraylist
		//https://docs.oracle.com/en/solutions/develop-microservice-java-app/develop-application1.html#GUID-E7D970E1-D1A2-4908-948A-858BE453A3F0
		CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>();
		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
		if (!microserviceProperties.getDockerActive().equals("true")){
			attractions.addAll(ListTools.getAttractions());
		} else {
			attractions.addAll(gpsUtilRestService.getAttractions());
		}
		attractions.addAll(ListTools.getAttractions());
		userLocations.addAll(user.getVisitedLocations());

		userLocations.forEach((visitedLocation) -> {
			attractions.forEach((attraction) -> {
				//Task 1: make sure we don't already have attraction we work on
				if(user.getUserRewards().parallelStream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					//Task 2: make sure attraction is in the area of the location we work on
					if(nearAttraction(visitedLocation, attraction)) {
						//Task 3: add rewards
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			});
		});
	}
	
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
