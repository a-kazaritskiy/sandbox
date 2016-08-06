package com.example.sandbox.domain.deposit;

import com.example.sandbox.storage.DataStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepositConfiguration {

	@Bean
	public DepositService depositService(DataStorage dataStorage) {
		return new DepositServiceImpl(dataStorage);
	}
}