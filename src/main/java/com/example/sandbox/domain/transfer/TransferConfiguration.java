package com.example.sandbox.domain.transfer;

import com.example.sandbox.domain.fee.FeeService;
import com.example.sandbox.domain.rate.RateService;
import com.example.sandbox.storage.DataStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferConfiguration {

	@Bean
	public TransferService transferService(
		DataStorage dataStorage,
		RateService rateService,
		FeeService feeService
	) {
		return new TransferServiceImpl(dataStorage, rateService, feeService);
	}
}