package tourGuide;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import tourGuide.domain.Location;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import tourGuide.domain.VisitedLocation;
import tourGuide.service.TourGuideService;
import tourGuide.domain.User;
import tourGuide.web.dto.NearByUserAttractionDTO;
import tourGuide.web.dto.UserPreferencesDTO;
import tripPricer.Provider;

@RestController
public class TourGuideController {
    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

	@Autowired
	TourGuideService tourGuideService;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }

    /**
     * <b>Return User Location with the 5 closest attractions</b>
     * @param userName mandatory string
     * @return json object containing user actual location and the 5 closest attractions
     */
    @RequestMapping("/getNearbyAttractions")
    public NearByUserAttractionDTO getNearbyAttractions(@RequestParam String userName) {
        NearByUserAttractionDTO nearByUserAttractionDTO = tourGuideService.getNearByAttractions(getUser(userName));
        logger.info("Call getNearbyAttractions endpoint (get user's nearby attractions)");
    	return nearByUserAttractionDTO;
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    /**
     * <b>Return all users with their actual locations</b>
     * <p>return json object with UserId and longitude and latitude per each</p>
     *
     * @return all locations of users who are using TourGuide
     */
    @RequestMapping("/getAllCurrentLocations")
    public Map<String, Location> getAllCurrentLocations() {
        Map<String, Location> currentLocations = tourGuideService.getAllCurrentLocations();
        logger.info("Call getAllCurrentLocations endpoint (get all users locations)");
    	return currentLocations;
    }

    /**
     * <b>Return a list of Trip providers = trip deals personalised with user preferences</b>
     * @param userName mandatory string
     * @return json with providers information
     */
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        logger.info("Call getTripDeals endpoint (get user's personalized trip deals)");
    	return providers;
    }

    /**
     * <b>set user Preferences and return user updated</b>
     * @param userName mandatory string
     * @param userPreferences user preferences
     * @return user updated
     */
    @RequestMapping("/setUserPreferences")
    public Map<String, UserPreferencesDTO> setUserPreferences(@RequestParam String userName,
                                     @RequestBody UserPreferencesDTO userPreferences) {
        Map<String, UserPreferencesDTO> userUpdated = tourGuideService.setUserPreferences(userName, userPreferences);
        logger.info("Call setUserPreferences endpoint");
        return userUpdated;
    }

    /**
     * <b>get user Preferences</b>
     * @param userName mandatory string
     * @return user preferences
     */
    @RequestMapping("/getUserPreferences")
    public UserPreferencesDTO getUserPreferences(@RequestParam String userName) {
        User user = tourGuideService.getUser(userName);
        UserPreferencesDTO userPreferences = UserPreferencesDTO.convertToDTO(user.getUserPreferences());
        logger.info("Call getUserPreferences endpoint");
        return userPreferences;
    }
    
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}