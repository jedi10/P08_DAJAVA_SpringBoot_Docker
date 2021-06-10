package tourGuide.service.restTemplateService;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tourGuide.configuration.MicroserviceProperties;
import tourGuide.domain.Attraction;
import tourGuide.domain.Location;
import tourGuide.domain.VisitedLocation;
import tourGuide.tool.ListTools;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(GpsUtilRestService.class)
@AutoConfigureWebClient(registerRestTemplate = true)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GpsUtilRestServiceTest {

    private MicroserviceProperties microserviceProperties;

    @Autowired
    private GpsUtilRestService gpsUtilRestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockRestServiceServer mockServer;

    @Mock
    private RestTemplate restTemplateMock;

    @BeforeEach
    private void beforeEach(){
        this.mockServer.reset();
        microserviceProperties = new MicroserviceProperties();
        gpsUtilRestService.microserviceProperties = microserviceProperties;
        //gpsUtilRestService = new GpsUtilRestService(microserviceProperties, restTemplateMock);
    }
    @AfterEach
    private void afterEach(){
        microserviceProperties = null;
        //gpsUtilRestService.microserviceProperties = microserviceProperties;
    }


    @Order(1)
    @Test
    public void testMicroserviceProperties() {
        assertEquals("http://localhost:", microserviceProperties.getAddress());
    }

    @Order(2)
    @Test
    public void getUserLocation_mockRestServiceServerTest() throws JsonProcessingException {
        //Given
        double longitude = 120.567;
        double latitude = 75.789;
        UUID userUuid = UUID.randomUUID();
        VisitedLocation visitedLocationGiven = new VisitedLocation(userUuid,
                new Location(longitude, latitude),
                new Date());
        String httpUrl = String.format("%s%s%s",
                microserviceProperties.getAddress(),
                microserviceProperties.getGpsutilPort(),
                GpsUtilRestService.userLocationURL);
        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.fromHttpUrl(httpUrl).
                        queryParam("userId", userUuid);
        String httpUri = uriComponentsBuilder.toUriString();
        String json = this.objectMapper
                .writeValueAsString(visitedLocationGiven);
        this.mockServer
                .expect(requestTo(httpUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        //When
        VisitedLocation visitedLocationResult = gpsUtilRestService.getUserLocation(userUuid);

        //Then
        this.mockServer.verify();
        assertNotNull(visitedLocationResult);
        assertEquals(visitedLocationGiven, visitedLocationResult);
    }




    @Order(3)
    @Test
    public void getAttractions_mockRestServiceServerTest() throws JsonProcessingException {
        //Given
        List<Attraction> attractionsGiven = new ArrayList();
        attractionsGiven.add(new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D));
        attractionsGiven.add(new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767D, -110.821999D));
        attractionsGiven.add(new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689D, -115.510399D));

        StringBuilder httpUrl = new StringBuilder();
        httpUrl.append(microserviceProperties.getAddress());
        httpUrl.append(microserviceProperties.getGpsutilPort());
        httpUrl.append(GpsUtilRestService.attractionsURL);

        String json = this.objectMapper
                .writeValueAsString(attractionsGiven);
        this.mockServer
                .expect(requestTo(httpUrl.toString()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        //When
        List<Attraction> attractionsResult = gpsUtilRestService.getAttractions();

        //Then
        this.mockServer.verify();
        assertNotNull(attractionsResult);
        assertEquals(attractionsGiven, attractionsResult);
    }

    @Order(4)
    @Test
    public void getAttractions_cacheTest() throws JsonProcessingException {
        //Given
        List<Attraction> attractionsGiven = new ArrayList();
        attractionsGiven.add(new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D));
        attractionsGiven.add(new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767D, -110.821999D));
        attractionsGiven.add(new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689D, -115.510399D));
        gpsUtilRestService.setCacheAttractionList(attractionsGiven);

        //When
        List<Attraction> attractionsResult = gpsUtilRestService.getAttractions();

        //Then
        assertNotNull(attractionsResult);
        assertEquals(attractionsGiven, attractionsResult);
    }

    /**
     @Order(2)
     @Test
     public void getUserLocation_testWithMockito() {
     //Given
     double longitude = 120.567;
     double latitude = 75.789;
     UUID userUuid = UUID.randomUUID();
     VisitedLocation givenVisitedLocation = new VisitedLocation(userUuid,
     new Location(longitude, latitude),
     new Date());
     //https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
     ResponseEntity responseEntity = new ResponseEntity(givenVisitedLocation, HttpStatus.OK);
     UriComponentsBuilder uriComponentsBuilder =
     UriComponentsBuilder.fromHttpUrl("http://localhost:8095/userLocation").
     queryParam("userId", userUuid);
     String httpUri = uriComponentsBuilder.toUriString();
     Mockito.when(restTemplateMock.getForEntity(
     httpUri, VisitedLocation.class)).thenReturn(responseEntity);

     //When
     VisitedLocation visitedLocationResult = gpsUtilRestService.getUserLocation(UUID.randomUUID());

     //Then
     assertNotNull(visitedLocationResult);
     assertEquals(givenVisitedLocation, visitedLocationResult);
     }**/

    /**
     @Order(3)
     @Test
     public void getAttractions_mockitoTest() throws JsonProcessingException {
     //Given
     Attraction[] attractionsGiven = {
     new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D),
     new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767D, -110.821999D),
     new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689D, -115.510399D)
     };

     ResponseEntity<Attraction[]> responseEntity =
     new ResponseEntity<Attraction[]>(attractionsGiven, HttpStatus.ACCEPTED);
     Mockito.when(restTemplateMock.exchange(
     ArgumentMatchers.anyString(),
     ArgumentMatchers.any(HttpMethod.class),
     ArgumentMatchers.any(),
     ArgumentMatchers.<Class<String>>any()))
     .thenReturn(responseEntity);
     gpsUtilRestService.restTemplate = restTemplateMock;


     StringBuilder httpUrl = new StringBuilder();
     httpUrl.append(microserviceProperties.getAddress());
     httpUrl.append(microserviceProperties.getGpsutilPort());
     httpUrl.append(GpsUtilRestService.attractionsURL);

     UriComponentsBuilder uriComponentsBuilder =
     UriComponentsBuilder.fromHttpUrl(httpUrl.toString());
     String httpUri = uriComponentsBuilder.toUriString();
     String json = this.objectMapper
     .writeValueAsString(attractionsGiven);
     this.mockServer
     .expect(requestTo(httpUri))
     .andExpect(method(HttpMethod.GET))
     .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

     //When
     List<Attraction> attractionsResult = gpsUtilRestService.getAttractions();

     //Then
     this.mockServer.verify();
     assertNotNull(attractionsResult);
     assertEquals(attractionsGiven, attractionsResult);
     }**/
}

//https://rieckpil.de/testing-your-spring-resttemplate-with-restclienttest/
//https://www.baeldung.com/spring-mock-rest-template
//https://thetopsites.net/article/50619201.shtml
//https://www.programcreek.com/java-api-examples/?api=org.springframework.core.ParameterizedTypeReference