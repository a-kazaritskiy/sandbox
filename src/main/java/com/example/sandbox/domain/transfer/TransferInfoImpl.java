package com.example.sandbox.domain.transfer;

import com.google.common.base.Objects;

import java.math.BigDecimal;

public class TransferInfoImpl implements TransferInfo {

	final String fromAccount;
	final String toAccount;
	final BigDecimal toAmount;

	public TransferInfoImpl(String fromAccount, String toAccount, BigDecimal toAmount) {
		this.fromAccount = fromAccount;
		this.toAmount = toAmount;
		this.toAccount = toAccount;
	}

	@Override
	public String getFromAccount() {
		return fromAccount;
	}

	@Override
	public String getToAccount() {
		return toAccount;
	}

	@Override
	public BigDecimal getToAmount() {
		return toAmount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransferInfoImpl that = (TransferInfoImpl) o;
		return Objects.equal(fromAccount, that.fromAccount) &&
			Objects.equal(toAccount, that.toAccount) &&
			Objects.equal(toAmount, that.toAmount);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(fromAccount, toAccount, toAmount);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("fromAccount", fromAccount)
			.add("toAccount", toAccount)
			.add("toAmount", toAmount)
			.toString();
	}
}