package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.configuration.MicroserviceProperties;
import tourGuide.service.restTemplateService.GpsUtilRestService;
import tourGuide.tool.GpsUtilLocal;
import tourGuide.domain.Location;
import tourGuide.domain.VisitedLocation;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rewardCentral.RewardCentral;
import tourGuide.domain.User;
import tourGuide.domain.UserPreferences;
import tourGuide.helper.InternalTestHelper;
import tourGuide.web.dto.NearByAttractionDTO;
import tourGuide.web.dto.NearByUserAttractionDTO;
import tourGuide.web.dto.UserPreferencesDTO;
import tripPricer.Provider;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <b>Important: Need Microservice GpsUtil</b>
 * <p>Docker has to be active to load in container GpsUtil Microservice Project</p>
 * <p>You can pass over the docker resource by using the application in local mode: turn MicroserviceProperties.dockerActive to false</p>
 * @see MicroserviceProperties#getDockerActive()
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TourGuideServiceTest {

    private GpsUtilLocal gpsUtilLocal;

    @Autowired
    private GpsUtilRestService gpsUtilRestService;
    private RewardCentral rewardCentral;
    private RewardsService rewardsService;
    private TourGuideService tourGuideService;

    @BeforeEach
    private void beforeEach(){
        gpsUtilLocal = new GpsUtilLocal();
        rewardCentral = new RewardCentral();
        rewardsService = new RewardsService(gpsUtilLocal, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        tourGuideService = new TourGuideService(gpsUtilLocal, gpsUtilRestService, rewardsService, rewardCentral);
        tourGuideService.microserviceProperties = new MicroserviceProperties();

    }
    @AfterEach
    private void afterEach(){
        tourGuideService = null;
        rewardsService = null;
        rewardCentral = null;
        gpsUtilLocal = null;
    }

    /**
     * <b>Important: Need Microservice GpsUtil</b>
     */
    @Order(1)
    @Test
    void getAllCurrentLocations() {
        //Given
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);
        tourGuideService.tracker.stopTracking();

        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
        VisitedLocation visitedLocation2 = tourGuideService.trackUserLocation(user2);

        List<User> allUsers = tourGuideService.getAllUsers();
        assertTrue(allUsers.size() == 2);
        assertNotNull(visitedLocation);
        assertNotNull(visitedLocation2);

        //When
        Map<String, Location> currentLocationsMap = tourGuideService.getAllCurrentLocations();

        //Then
        assertNotNull(currentLocationsMap);
        assertEquals(2, currentLocationsMap.size());
        //Be sure to get users created in this test
        int userIndex = tourGuideService.getAllUsers().indexOf(user);
        int user2Index = tourGuideService.getAllUsers().indexOf(user2);
        User userResult = tourGuideService.getAllUsers().get(userIndex);
        User user2Result = tourGuideService.getAllUsers().get(user2Index);
        assertEquals(visitedLocation.location.latitude,
                currentLocationsMap.get(userResult.getUserId().toString()).latitude, 0.0);
        assertEquals(visitedLocation.location.longitude,
                currentLocationsMap.get(userResult.getUserId().toString()).longitude, 0.0);
        assertEquals(visitedLocation2.location.latitude,
                currentLocationsMap.get(user2Result.getUserId().toString()).latitude, 0.0);
        assertEquals(visitedLocation2.location.longitude,
                currentLocationsMap.get(user2Result.getUserId().toString()).longitude, 0.0);
    }

    /**
     * <b>Important: Need Microservice GpsUtil</b>
     */
    @Order(2)
    @Test
    public void getUserLocation() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();
        assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    @Order(3)
    @Test
    public void addUser() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        User retrivedUser = tourGuideService.getUser(user.getUserName());
        User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

        tourGuideService.tracker.stopTracking();

        assertEquals(user, retrivedUser);
        assertEquals(user2, retrivedUser2);
    }

    @Order(4)
    @Test
    public void getAllUsers() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        List<User> allUsers = tourGuideService.getAllUsers();

        tourGuideService.tracker.stopTracking();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    /**
     * <b>Important: Need Microservice GpsUtil</b>
     */
    @Order(5)
    @Test
    public void trackUser() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    /**
     * <b>Important: Need Microservice GpsUtil</b>
     */
    @Order(6)
    @Test
    public void getNearbyAttractions() {
        //GIVEN
        User user = new User(UUID.randomUUID(), "internalUser1", "000", "jon@tourGuide.com");

        //WHEN
        NearByUserAttractionDTO nearByUserAttractionDTO = tourGuideService.getNearByAttractions(user);
        tourGuideService.tracker.stopTracking();
        //THEN
        assertNotNull(nearByUserAttractionDTO);
        List<NearByAttractionDTO> nearByAttractionDTOList = nearByUserAttractionDTO.getTourist_attractions();
        assertNotNull(nearByAttractionDTOList);
        assertEquals(5, nearByAttractionDTOList.size());
    }

    @Order(7)
    @Test
    public void getTripDeals() {
        //Given
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        //When
        List<Provider> providers = tourGuideService.getTripDeals(user);
        tourGuideService.tracker.stopTracking();
        //Then
        assertEquals(5, providers.size());
    }

    @Order(8)
    @DisplayName("Trip Deal with Price Preference")
    @ParameterizedTest(name = "Between {0} and {1}")
    @CsvSource({"0, 2500", "2500, 5000"})
    public void getTripDealsWithPricePreference(int lowPricePreference, int highPricePreference) {
        //Given
        CurrencyUnit currency = Monetary.getCurrency("USD");
        Money moneyLow = Money.of(lowPricePreference, Monetary.getCurrency("USD"));
        Money moneyHigh = Money.of(highPricePreference, Monetary.getCurrency("USD"));
        UserPreferences userPreferences = new UserPreferences(1000, currency,
                moneyLow, moneyHigh,
                5, 5, 2,3);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        user.setUserPreferences(userPreferences);
        double userHighPrice = user.getUserPreferences().getHighPricePoint().getNumber().doubleValue();
        double userLowPrice = user.getUserPreferences().getLowerPricePoint().getNumber().doubleValue();
        assertNotNull(userHighPrice);
        assertNotEquals(0.00, userHighPrice);

        //When
        List<Provider> providers = tourGuideService.getTripDeals(user);
        tourGuideService.tracker.stopTracking();
        //Then
        providers.forEach(p -> {
            assertTrue(p.price <= userHighPrice,
                    "Provider price not respect user high price '"+ userHighPrice +"'");
            assertTrue(p.price >= userLowPrice,
                    "Provider price not respect user low price '"+ userHighPrice +"'");
        });
    }

    @Order(9)
    @DisplayName("Set User Preferences")
    @ParameterizedTest(name = "Between {0} and {1}")
    @CsvSource({"0, 2500", "2500, 5000"})
    void setUserPreferences(int lowPricePreference, int highPricePreference) {
        //Given
        UserPreferencesDTO userPreferencesGiven = new UserPreferencesDTO(1000, "USD",
                lowPricePreference, highPricePreference,
                5, 5, 2,3);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        tourGuideService.addUser(user);//without updated preferences

        //When
        Map<String, UserPreferencesDTO> userUpdated = tourGuideService.setUserPreferences(user.getUserName(), userPreferencesGiven);
        tourGuideService.tracker.stopTracking();

        //THEN
        assertNotNull(userUpdated);
        assertEquals(userPreferencesGiven.getHighPricePoint(),
                (userUpdated.get(user.getUserName()).getHighPricePoint()));
    }
}