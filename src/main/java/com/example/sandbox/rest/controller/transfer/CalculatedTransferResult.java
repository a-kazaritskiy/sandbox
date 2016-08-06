package com.example.sandbox.rest.controller.transfer;

import java.math.BigDecimal;
import java.util.Currency;

class CalculatedTransferResult {

	private final AccountSum from;
	private final AccountSum to;
	private final String rate;
	private final String fee;

	CalculatedTransferResult(
		String fromAccount,
		Currency fromCurrency,
		BigDecimal fromAmount,
		String toAccount,
		Currency toCurrency,
		BigDecimal toAmount,
		BigDecimal rate,
		BigDecimal fee
	) {
		this.from = AccountSum.of(fromAccount, fromCurrency, fromAmount);
		this.to = AccountSum.of(toAccount, toCurrency, toAmount);
		this.rate = rate.toPlainString();
		this.fee = fee.toPlainString();
	}

	public AccountSum getFrom() {
		return from;
	}

	public AccountSum getTo() {
		return to;
	}

	public String getRate() {
		return rate;
	}

	public String getFee() {
		return fee;
	}
}