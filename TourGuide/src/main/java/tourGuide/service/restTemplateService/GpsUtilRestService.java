package tourGuide.service.restTemplateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GpsUtilRestService {
    private final Logger logger = LoggerFactory.getLogger(GpsUtilRestService.class);

    private RestTemplate restTemplate;

    public GpsUtilRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
