package com.example.sandbox.domain.deposit;

import com.example.sandbox.domain.operation.Operation;

import java.math.BigDecimal;

public interface DepositOperation extends Operation {

	String getAccount();

	BigDecimal getAmount();
}