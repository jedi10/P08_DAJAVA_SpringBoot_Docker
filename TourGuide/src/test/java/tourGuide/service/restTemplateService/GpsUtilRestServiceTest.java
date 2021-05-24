package tourGuide.service.restTemplateService;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import tourGuide.configuration.MicroserviceProperties;
import tourGuide.domain.VisitedLocation;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GpsUtilRestServiceTest {

    @Autowired
    private MicroserviceProperties microserviceProperties;

    @MockBean
    private RestTemplate restTemplate;

    private GpsUtilRestService gpsUtilRestService;

    @BeforeAll
    private void beforeAll(){
        gpsUtilRestService = new GpsUtilRestService(microserviceProperties, restTemplate);
    }


    @Order(1)
    @Test
    void testMicroserviceProperties() {
        assertEquals("http://localhost:", microserviceProperties.getAddress());

    }

    @Order(2)
    @Test
    void getUserLocation() {
        //When
        VisitedLocation visitedLocation = gpsUtilRestService.getUserLocation(UUID.randomUUID());

        //Then
        assertNull(visitedLocation);
    }
}