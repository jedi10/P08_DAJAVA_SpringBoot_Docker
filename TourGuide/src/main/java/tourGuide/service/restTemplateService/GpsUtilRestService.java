package tourGuide.service.restTemplateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tourGuide.configuration.MicroserviceProperties;
import tourGuide.domain.Attraction;
import tourGuide.domain.VisitedLocation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilRestService {
    private final Logger logger = LoggerFactory.getLogger(GpsUtilRestService.class);

    MicroserviceProperties microserviceProperties;
    //final String defaultGpsUtilRootUrl = "http://localhost:8090";
    @Autowired
    RestTemplate restTemplate;

    private List<Attraction> attractionListCache = null;
    private LocalDateTime attractionListTimeRefresh;

    public GpsUtilRestService() {
        microserviceProperties = new MicroserviceProperties();
    }

    public static final String className = GpsUtilRestService.class.getSimpleName();
    public static final String userLocationURL = "/userLocation";
    public static final String attractionsURL = "/attractions";

    /**
     * <b>getUserLocation for a given user</b>
     *
     * @param userId userId mandatory
     * @return VisitedLocation for given user
     */
    public VisitedLocation getUserLocation(UUID userId) {
        String logMessage = String.format("Call to %s.getUserLocation(%s)",
                className,
                userId);
        logger.debug(logMessage);
        //System.out.print("url: "+ microserviceProperties.getAddress() + microserviceProperties.getGpsutilPort());
        String httpUrl = String.format("%s%s%s",
                microserviceProperties.getAddress(),
                microserviceProperties.getGpsutilPort(),
                userLocationURL);
        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.fromHttpUrl(httpUrl).
                queryParam("userId", userId);
        String httpUri = uriComponentsBuilder.toUriString();

        try {
            ResponseEntity<VisitedLocation> responseEntity = restTemplate.getForEntity(
                            httpUri,
                            VisitedLocation.class);
            return responseEntity.getBody();
        } catch (RestClientException exception) {
            String errorMessage = String.format("Exception during %s.getUserLocation : %s",
                    className,
                    exception.getMessage());
            logger.error(errorMessage);
            return null;
        }
    }

    /**
     * <b>get list of all Attractions</b>
     *
     * @return list of attractions
     */
    public List<Attraction> getAttractions() {
        List<Attraction> result = null;
        if (!cacheForAttractionListNeedRefresh()){
            logger.debug("Call to "+className+".getAttractions() from cache");
            return this.attractionListCache;
        }
        logger.debug("Call to "+className+".getAttractions()");

        StringBuilder httpUrl = new StringBuilder();
        httpUrl.append(microserviceProperties.getAddress());
        httpUrl.append(microserviceProperties.getGpsutilPort());
        httpUrl.append(attractionsURL);
        try {
            ResponseEntity<Attraction[]> responseEntity = restTemplate.getForEntity(
                    httpUrl.toString(),
                    Attraction[].class);
            /**ResponseEntity<Attraction[]> responseEntity = restTemplate.exchange(
                    httpUrl.toString(),
                    HttpMethod.GET,
                    null,//https://springbootdev.com/2017/11/21/spring-resttemplate-exchange-method/
                    Attraction[].class);**/
            /**ResponseEntity<List<Attraction>> responseEntity = restTemplate.exchange(
             httpUrl.toString(),
             HttpMethod.GET,
             null,//https://springbootdev.com/2017/11/21/spring-resttemplate-exchange-method/
             new ParameterizedTypeReference<>() {}
             );**/
            if (responseEntity != null && responseEntity.getBody() != null){
                setCacheAttractionList(Arrays.asList(responseEntity.getBody()));
                result = this.attractionListCache;
                logger.debug("Found {} Attractions", result.size());
            } else {
                logger.debug("Return Zero Attraction");
            }
            return result;
        } catch (RestClientException exception) {
            String errorMessage = String.format("Exception during %s.getAttractions : %s",
                    className,
                    exception.getMessage());
            logger.error(errorMessage);
            return null;
        }
    }

    public void setCacheAttractionList(List<Attraction> attractionListCache){
        this.attractionListCache = attractionListCache;
        this.attractionListTimeRefresh = LocalDateTime.now();
    }

    private boolean cacheForAttractionListNeedRefresh(){
        Boolean result = true;
        if (this.attractionListCache != null){
            Duration duration = Duration.between(this.attractionListTimeRefresh, LocalDateTime.now());
            if (duration.toMinutes() < 10){
                result = false;
            }
        }
        return result;
    }

    private static Period getPeriod(LocalDateTime dob, LocalDateTime now) {
        //https://stackoverflow.com/questions/25747499/java-8-difference-between-two-localdatetime-in-multiple-units
        return Period.between(dob.toLocalDate(), now.toLocalDate());
    }
}


//https://www.concretepage.com/spring-5/spring-resttemplate-exchange#Get