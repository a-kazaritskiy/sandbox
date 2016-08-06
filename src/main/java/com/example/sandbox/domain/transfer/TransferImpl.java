package com.example.sandbox.domain.transfer;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.util.Currency;

public class TransferImpl extends TransferInfoImpl implements Transfer {

	final Currency fromCurrency;
	final BigDecimal fromAmount;
	final Currency toCurrency;
	final BigDecimal rate;
	final BigDecimal fee;

	public TransferImpl(
		TransferInfo transferInfo,
		Currency fromCurrency,
		BigDecimal fromAmount,
		Currency toCurrency,
		BigDecimal rate,
		BigDecimal fee
	) {
		super(transferInfo.getFromAccount(), transferInfo.getToAccount(), transferInfo.getToAmount());
		this.fromCurrency = fromCurrency;
		this.fromAmount = fromAmount;
		this.toCurrency = toCurrency;
		this.rate = rate;
		this.fee = fee;
	}

	public TransferImpl(
		String fromAccount,
		Currency fromCurrency,
		BigDecimal fromAmount,
		String toAccount,
		Currency toCurrency,
		BigDecimal toAmount,
		BigDecimal rate,
		BigDecimal fee
	) {
		super(fromAccount, toAccount, toAmount);
		this.fromCurrency = fromCurrency;
		this.fromAmount = fromAmount;
		this.toCurrency = toCurrency;
		this.rate = rate;
		this.fee = fee;
	}

	@Override
	public Currency getFromCurrency() {
		return fromCurrency;
	}

	@Override
	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	@Override
	public Currency getToCurrency() {
		return toCurrency;
	}

	@Override
	public BigDecimal getRate() {
		return rate;
	}

	@Override
	public BigDecimal getFee() {
		return fee;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransferImpl transfer = (TransferImpl) o;
		return Objects.equal(fromAccount, transfer.fromAccount) &&
			Objects.equal(fromCurrency, transfer.fromCurrency) &&
			Objects.equal(fromAmount, transfer.fromAmount) &&
			Objects.equal(toAccount, transfer.toAccount) &&
			Objects.equal(toCurrency, transfer.toCurrency) &&
			Objects.equal(toAmount, transfer.toAmount) &&
			Objects.equal(rate, transfer.rate) &&
			Objects.equal(fee, transfer.fee);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			fromAccount,
			fromCurrency,
			fromAmount,
			toAccount,
			toCurrency,
			toAmount,
			rate,
			fee
		);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("fromAccount", fromAccount)
			.add("fromCurrency", fromCurrency)
			.add("fromAmount", fromAmount)
			.add("toAccount", toAccount)
			.add("toCurrency", toCurrency)
			.add("toAmount", toAmount)
			.add("rate", rate)
			.add("fee", fee)
			.toString();
	}
}