package com.example.sandbox.domain.balance;

import com.example.sandbox.domain.account.AccountNotFoundException;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;

public interface BalanceService {

	BigDecimal getBalance(String account)
		throws DataAccessException, AccountNotFoundException;
}