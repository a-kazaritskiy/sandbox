package com.example.sandbox.storage;

import com.example.sandbox.domain.account.Account;
import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.domain.transfer.Transfer;
import com.example.sandbox.domain.transfer.TransferOperation;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.Currency;

public interface DataStorage {

	Account findAccount(String id)
		throws DataAccessException, AccountNotFoundException;

	Currency getAccountCurrency(String id)
		throws DataAccessException, AccountNotFoundException;

	BigDecimal getBalance(String id)
		throws DataAccessException, AccountNotFoundException;

	TransferOperation transfer(Transfer transfer)
		throws DataAccessException, AccountNotFoundException;
}