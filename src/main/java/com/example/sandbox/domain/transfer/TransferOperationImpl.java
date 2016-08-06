package com.example.sandbox.domain.transfer;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.util.Currency;

public class TransferOperationImpl extends TransferImpl implements TransferOperation {

	private final Long id;
	private final String status;
	private final String code;

	public TransferOperationImpl(
		Long id,
		String status,
		String code,
		Transfer transfer
	) {
		this(id,
			status,
			code,
			transfer.getFromAccount(),
			transfer.getFromCurrency(),
			transfer.getFromAmount(),
			transfer.getToAccount(),
			transfer.getToCurrency(),
			transfer.getToAmount(),
			transfer.getRate(),
			transfer.getFee());
	}

	public TransferOperationImpl(
		Long id,
		String status,
		String code,
		String fromAccount,
		Currency fromCurrency,
		BigDecimal fromAmount,
		String toAccount,
		Currency toCurrency,
		BigDecimal toAmount,
		BigDecimal rate,
		BigDecimal fee
	) {
		super(fromAccount, fromCurrency, fromAmount, toAccount, toCurrency, toAmount, rate, fee);
		this.id = id;
		this.status = status;
		this.code = code;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransferOperationImpl transfer = (TransferOperationImpl) o;
		return Objects.equal(id, transfer.id) &&
			Objects.equal(status, transfer.status) &&
			Objects.equal(code, transfer.code) &&
			Objects.equal(fromAccount, transfer.fromAccount) &&
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
			id,
			status,
			code,
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
			.add("id", id)
			.add("status", status)
			.add("code", code)
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