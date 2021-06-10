package tourGuide.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import tourGuide.configuration.MicroserviceProperties;
import tourGuide.service.restTemplateService.GpsUtilRestService;
import tourGuide.tool.GpsUtilLocal;
import rewardCentral.RewardCentral;
import tourGuide.service.RewardsService;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(MicroserviceProperties.class)
public class TourGuideModule {
	
	@Bean
	public GpsUtilLocal getGpsUtil() {
		return new GpsUtilLocal();
	}
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getGpsUtilService(), getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}

	@Bean(name="defaultRest")
	@Primary
	public RestTemplate requestRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(2))
				.setReadTimeout(Duration.ofSeconds(2))
				.build();
	}

	@Bean(name="requestRestTemplate2")
	public RestTemplate requestRestTemplate2(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(2))
				.setReadTimeout(Duration.ofSeconds(2))
				.build();
	}

	@Bean
	public GpsUtilRestService getGpsUtilService() {
		return new GpsUtilRestService();
	}

}


//https://www.baeldung.com/spring-qualifier-annotation