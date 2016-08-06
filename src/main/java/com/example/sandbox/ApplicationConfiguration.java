package com.example.sandbox;

import com.example.sandbox.domain.account.AccountConfiguration;
import com.example.sandbox.domain.balance.BalanceConfiguration;
import com.example.sandbox.domain.deposit.DepositConfiguration;
import com.example.sandbox.domain.fee.FeeConfiguration;
import com.example.sandbox.domain.rate.RateConfiguration;
import com.example.sandbox.domain.transfer.TransferConfiguration;
import com.example.sandbox.storage.DataStorageConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	AccountConfiguration.class,
	BalanceConfiguration.class,
	DepositConfiguration.class,
	TransferConfiguration.class,
	RateConfiguration.class,
	FeeConfiguration.class,
	DataStorageConfiguration.class
})
class ApplicationConfiguration {
}