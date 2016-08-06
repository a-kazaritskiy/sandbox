package com.example.sandbox.rest.controller.account;

import java.math.BigDecimal;
import java.util.Currency;

class AccountResult {

	private final String id;
	private final String currency;
	private final String balance;

	static AccountResult of(String id, Currency currency, BigDecimal amount) {
		return new AccountResult(id, currency.getCurrencyCode(), amount.toPlainString());
	}

	private AccountResult(String id, String currency, String balance) {
		this.id = id;
		this.currency = currency;
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public String getCurrency() {
		return currency;
	}

	public String getBalance() {
		return balance;
	}
}