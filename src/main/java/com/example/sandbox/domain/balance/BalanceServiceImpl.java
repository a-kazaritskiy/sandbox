package com.example.sandbox.domain.balance;

import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.storage.DataStorage;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;

class BalanceServiceImpl implements BalanceService {

	private final DataStorage dataStorage;

	BalanceServiceImpl(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
	}

	@Override
	public BigDecimal getBalance(String account) throws DataAccessException, AccountNotFoundException {
		return dataStorage.getBalance(account);
	}
}