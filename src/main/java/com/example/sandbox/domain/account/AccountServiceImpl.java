package com.example.sandbox.domain.account;

import com.example.sandbox.storage.DataStorage;
import org.springframework.dao.DataAccessException;

import java.util.Currency;

class AccountServiceImpl implements AccountService {

	private final DataStorage dataStorage;

	AccountServiceImpl(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
	}

	@Override
	public Account find(String id) throws DataAccessException, AccountNotFoundException {
		return dataStorage.findAccount(id);
	}

	@Override
	public Currency getCurrency(String id) throws DataAccessException, AccountNotFoundException {
		return dataStorage.getAccountCurrency(id);
	}
}