package com.example.sandbox.domain.deposit;

import com.example.sandbox.domain.account.AccountNotFoundException;

import java.math.BigDecimal;

// Assume we can make a deposit from somewhere outside
public interface DepositService {

	DepositOperation make(String account, BigDecimal amount)
		throws AccountNotFoundException;
}