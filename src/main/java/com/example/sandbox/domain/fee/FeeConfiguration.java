package com.example.sandbox.domain.fee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeeConfiguration {

	@Bean
	public FeeService feeService() {
		return new GenerousFeeService();
	}
}