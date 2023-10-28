package org.prgrms.nabimarketbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// @Bean
	// public ObjectMapper objectMapper() {
	// 	return new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
	// }

}
