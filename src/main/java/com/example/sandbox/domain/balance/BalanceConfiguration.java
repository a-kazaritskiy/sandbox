package com.example.sandbox.domain.balance;

import com.example.sandbox.storage.DataStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BalanceConfiguration {

	@Bean
	public BalanceService balanceService(DataStorage dataStorage) {
		return new BalanceServiceImpl(dataStorage);
	}
}