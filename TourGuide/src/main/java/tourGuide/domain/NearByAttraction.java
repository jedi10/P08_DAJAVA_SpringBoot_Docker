package tourGuide.domain;

import gpsUtil.location.Attraction;

import java.util.UUID;

/**
 * <b>Used with TourGuideService.getNearByAttractions Service</b>
 * @see tourGuide.service.TourGuideService#getNearByAttractions(User)
 */
public class NearByAttraction {
    private String attractionName;
    private Double attractionLat;
    private Double attractionLong;
    private Double distance;
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

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }
}


//https://www.javaguides.net/2019/04/jackson-ignore-fields-or-properties-on-serialization.html