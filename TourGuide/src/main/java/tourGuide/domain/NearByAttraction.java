package tourGuide.domain;

import gpsUtil.location.Attraction;
import rewardCentral.RewardCentral;

/**
 * <b>Used with NearByUserAttraction</b>
 */
public class NearByAttraction {
    private String attractionName;
    private Double attractionLat;
    private Double attractionLong;
    private Double distance;
    private Double rewardsPoints;

    /**
     * <b>Constructor NearByAttraction</b>
     * @param attraction mandatory - Attraction Type
     * @param distance mandatory - Double Type
     * @param user mandatory - User Type     *
     */
    public NearByAttraction(Attraction attraction, Double distance, User user) {
        this.attractionName = attraction.attractionName;
        this.attractionLat = attraction.latitude;
        this.attractionLong = attraction.longitude;
        this.distance = distance;
        this.rewardsPoints = calculateAttractionRewards(attraction, user);
    }

    private double calculateAttractionRewards(Attraction attraction, User user ){
        RewardCentral rewardCentral = new RewardCentral();
        return rewardCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public Double getAttractionLat() {
        return attractionLat;
    }

    public void setAttractionLat(Double attractionLat) {
        this.attractionLat = attractionLat;
    }

    public Double getAttractionLong() {
        return attractionLong;
    }

    public void setAttractionLong(Double attractionLong) {
        this.attractionLong = attractionLong;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getRewardsPoints() {
        return rewardsPoints;
    }

    public void setRewardsPoints(Double rewardsPoints) {
        this.rewardsPoints = rewardsPoints;
    }
}
