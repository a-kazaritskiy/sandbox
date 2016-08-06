package com.example.sandbox.rest.controller.transfer;

import java.math.BigDecimal;
import java.util.Currency;

class TransferResult {

	private final String id;
	private final String status;
	private final AccountSum from;
	private final AccountSum to;
	private final String rate;
	private final String fee;

	static TransferResult of(
		Long id,
		String status,
		String fromAccount,
		Currency fromCurrency,
		BigDecimal fromAmount,
		String toAccount,
		Currency toCurrency,
		BigDecimal toAmount,
		BigDecimal rate,
		BigDecimal fee
	) {
		return new TransferResult(
			String.valueOf(id),
			status,
			fromAccount,
			fromCurrency,
			fromAmount, toAccount,
			toCurrency,
			toAmount,
			rate,
			fee
		);
	}

	private TransferResult(
		String id,
		String status,
		String fromAccount,
		Currency fromCurrency,
		BigDecimal fromAmount,
		String toAccount,
		Currency toCurrency,
		BigDecimal toAmount,
		BigDecimal rate,
		BigDecimal fee
	) {
		this.id = id;
		this.status = status;
		this.from = AccountSum.of(fromAccount, fromCurrency, fromAmount);
		this.to = AccountSum.of(toAccount, toCurrency, toAmount);
		this.rate = rate.toPlainString();
		this.fee = fee.toPlainString();
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
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
