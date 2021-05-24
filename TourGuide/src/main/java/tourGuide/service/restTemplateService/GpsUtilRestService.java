package tourGuide.service.restTemplateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tourGuide.configuration.MicroserviceProperties;
import tourGuide.domain.VisitedLocation;

import java.util.UUID;

@Service
public class GpsUtilRestService {
    private final Logger logger = LoggerFactory.getLogger(GpsUtilRestService.class);

    private MicroserviceProperties microserviceProperties;

    private RestTemplate restTemplate;

    //final String defaultGpsUtilRootUrl = "http://localhost:8090";

    public GpsUtilRestService(MicroserviceProperties microserviceProperties, RestTemplate restTemplate) {
        this.microserviceProperties = microserviceProperties;
        this.restTemplate = restTemplate;
    }

    public VisitedLocation getUserLocation(UUID userId) {
        System.out.print("url: "+ microserviceProperties.getAddress() + microserviceProperties.getGpsutilPort());
        return null;
    }


}
