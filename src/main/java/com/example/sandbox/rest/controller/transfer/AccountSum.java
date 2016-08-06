package com.example.sandbox.rest.controller.transfer;

import java.math.BigDecimal;
import java.util.Currency;

@SuppressWarnings("WeakerAccess")
public class AccountSum {

	private final String account;
	private final String currency;
	private final String amount;

	static AccountSum of(String account, Currency currency, BigDecimal amount) {
		return new AccountSum(account, currency.getCurrencyCode(), amount.toPlainString());
	}

	private AccountSum(String account, String currency, String amount) {
		this.account = account;
		this.currency = currency;
		this.amount = amount;
	}

	public String getAccount() {
		return account;
	}

	public String getCurrency() {
		return currency;
	}

	public String getAmount() {
		return amount;
	}
}