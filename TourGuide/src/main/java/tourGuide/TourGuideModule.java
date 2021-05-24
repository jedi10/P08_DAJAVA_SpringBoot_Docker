package tourGuide;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.client.RestTemplate;
import tourGuide.configuration.MicroserviceProperties;
import tourGuide.tool.GpsUtilLocal;
import rewardCentral.RewardCentral;
import tourGuide.service.RewardsService;

@Configuration
@EnableConfigurationProperties(MicroserviceProperties.class)
public class TourGuideModule {
	
	@Bean
	public GpsUtilLocal getGpsUtil() {
		return new GpsUtilLocal();
	}
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
