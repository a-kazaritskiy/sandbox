package com.example.sandbox.domain.rate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateConfiguration {

	@Bean
	public RateService rateService() {
		return new FairRateService();
	}
}