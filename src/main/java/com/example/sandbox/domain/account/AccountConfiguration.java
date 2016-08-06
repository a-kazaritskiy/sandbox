package com.example.sandbox.domain.account;

import com.example.sandbox.storage.DataStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfiguration {

	@Bean
	public AccountService accountService(DataStorage dataStorage) {
		return new AccountServiceImpl(dataStorage);
	}
}