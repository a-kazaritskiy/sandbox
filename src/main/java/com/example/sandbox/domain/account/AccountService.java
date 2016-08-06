package com.example.sandbox.domain.account;

import org.springframework.dao.DataAccessException;

import java.util.Currency;

public interface AccountService {

	Account find(String id)
		throws DataAccessException, AccountNotFoundException;

	Currency getCurrency(String id)
		throws DataAccessException, AccountNotFoundException;
}