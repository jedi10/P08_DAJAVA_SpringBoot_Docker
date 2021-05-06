package tourGuide.web.dto;

import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.domain.NearByAttraction;
import tourGuide.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Used to format endpoint response /nearbyAttractions (POST)</b>
 * @see tourGuide.service.TourGuideService#getNearByAttractions(User)
 */
public class NearByUserAttractionDTO {

    private Double userLat;
    private Double userLong;
    private List<NearByAttractionDTO> tourist_attractions;

    /**
     * <b>Constructor</b>
     * @param user mandatory
     * @param userLocation mandatory
     * @param tourist_attractions mandatory
     */
    public NearByUserAttractionDTO(User user, VisitedLocation userLocation, List<NearByAttraction> tourist_attractions, RewardCentral rewardCentral) {
        this.userLat = userLocation.location.latitude;
        this.userLong = userLocation.location.longitude;
        convertNearByLocationToDTO(user, tourist_attractions, rewardCentral);
    }


    public void convertNearByLocationToDTO(User user, List<NearByAttraction> nearByAttractions, RewardCentral rewardCentral){
        List<NearByAttractionDTO> result = new ArrayList<>();
        nearByAttractions.forEach(nearByAttraction->
                result.add(new NearByAttractionDTO(nearByAttraction, user, rewardCentral)));
        tourist_attractions = result;
    }

    public Double getUserLat() {
        return userLat;
    }

    public void setUserLat(Double userLat) {
        this.userLat = userLat;
    }

    public Double getUserLong() {
        return userLong;
    }

    public void setUserLong(Double userLong) {
        this.userLong = userLong;
    }

    public List<NearByAttractionDTO> getTourist_attractions() {
        return tourist_attractions;
    }

    public void setTourist_attractions(List<NearByAttractionDTO> tourist_attractions) {
        this.tourist_attractions = tourist_attractions;
    }
}
