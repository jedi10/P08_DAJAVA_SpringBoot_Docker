package tourGuide.domain;

import gpsUtil.location.Attraction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * <b>Used with TourGuideService.getNearByAttractions Service</b>
 * @see tourGuide.service.TourGuideService#getNearByAttractions(User)
 */
@Getter
@Setter
public class NearByAttraction {
    private String attractionName;
    private Double attractionLat;
    private Double attractionLong;
    private Double distance;
    private UUID attractionId;

    /**
     * <b>Constructor NearByAttraction</b>
     *
     * @param attraction mandatory - Attraction Type
     * @param distance   mandatory - Double Type
     */
    public NearByAttraction(Attraction attraction, Double distance) {
        this.attractionName = attraction.attractionName;
        this.attractionLat = attraction.latitude;
        this.attractionLong = attraction.longitude;
        this.attractionId = attraction.attractionId;
        this.distance = distance;
    }
}


//https://www.javaguides.net/2019/04/jackson-ignore-fields-or-properties-on-serialization.html