package com.example.sandbox.rest.controller.account;

import java.math.BigDecimal;
import java.util.Currency;

class DepositResult {

	private final String id;
	private final String status;
	private final String currency;
	private final String amount;
	private final String balance;

	DepositResult(String id, String status, Currency currency, BigDecimal amount, BigDecimal balance) {
		this.id = id;
		this.status = status;
		this.currency = currency.getCurrencyCode();
		this.amount = amount.toPlainString();
		this.balance = balance.toPlainString();
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public String getCurrency() {
		return currency;
	}

	public String getAmount() {
		return amount;
	}

	public String getBalance() {
		return balance;
	}
}