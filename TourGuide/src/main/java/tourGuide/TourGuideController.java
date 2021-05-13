package tourGuide;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.domain.UserPreferences;
import tourGuide.service.TourGuideService;
import tourGuide.domain.User;
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
    
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getNearByAttractions(getUser(userName)));
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }
    
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	return JsonStream.serialize(tourGuideService.getAllCurrentLocations());
    }

    /**
     * <b>Return a list of Trip providers = trip deals personalised with user preferences</b>
     * @param userName mandatory
     * @return json with providers information
     */
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        logger.info("Call getTripDeals endpoint (get user's personalized trip deals)");
    	return JsonStream.serialize(providers);
    }

    /**
     * <b>set user Preferences and return user updated</b>
     * @param userName mandatory string
     * @param userPreferences user preferences
     * @return user updated
     */
    @RequestMapping("/setUserPreferences")
    public String setUserPreferences(@RequestParam String userName,
                                     @RequestBody UserPreferencesDTO userPreferences) {
        Map<String, UserPreferencesDTO> userUpdated = tourGuideService.setUserPreferences(userName, userPreferences);
        logger.info("Call setUserPreferences endpoint");
        return JsonStream.serialize(userUpdated);
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