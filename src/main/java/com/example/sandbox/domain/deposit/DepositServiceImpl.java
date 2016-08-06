package com.example.sandbox.domain.deposit;

import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.domain.transfer.TransferImpl;
import com.example.sandbox.domain.transfer.TransferOperation;
import com.example.sandbox.storage.DataStorage;

import java.math.BigDecimal;
import java.util.Currency;

class DepositServiceImpl implements DepositService {

	private final DataStorage dataStorage;

	DepositServiceImpl(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
	}

	@Override
	public DepositOperation make(String account, BigDecimal amount) throws AccountNotFoundException {
		// use system account, same currency, apply no fee
		Currency currency = dataStorage.getAccountCurrency(account);
		TransferOperation transfer = dataStorage.transfer(new TransferImpl(
			SYSTEM_ACCOUNT,
			currency,
			amount,
			account,
			currency,
			amount,
			BigDecimal.ONE,
			BigDecimal.ZERO
		));
		return new Deposit(transfer.getId(), transfer.getStatus(), transfer.getCode(), account, amount);
	}

	private static final String SYSTEM_ACCOUNT = "system";
}