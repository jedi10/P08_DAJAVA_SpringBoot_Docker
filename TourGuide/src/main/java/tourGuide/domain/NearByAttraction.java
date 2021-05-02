package tourGuide.domain;

import gpsUtil.location.Attraction;
import rewardCentral.RewardCentral;

import java.util.UUID;

/**
 * <b>Used with NearByUserAttraction</b>
 */
public class NearByAttraction {
    private String attractionName;
    private Double attractionLat;
    private Double attractionLong;
    private Double distance;
    private int rewardsPoints;
    private UUID attractionId;

    /**
     * <b>Constructor NearByAttraction</b>
     * @param attraction mandatory - Attraction Type
     * @param distance mandatory - Double Type
     */
    public NearByAttraction(Attraction attraction, Double distance) {
        this.attractionName = attraction.attractionName;
        this.attractionLat = attraction.latitude;
        this.attractionLong = attraction.longitude;
        this.attractionId = attraction.attractionId;
        this.distance = distance;
        this.rewardsPoints = 0;
    }

    public void calculateAttractionRewards(User user ){
        RewardCentral rewardCentral = new RewardCentral();
        this.rewardsPoints = rewardCentral.getAttractionRewardPoints(this.attractionId, user.getUserId());
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

    public int getRewardsPoints() {
        return rewardsPoints;
    }

    public void setRewardsPoints(int rewardsPoints) {
        this.rewardsPoints = rewardsPoints;
    }
}
