package com.example.sandbox.domain.deposit;

import com.google.common.base.Objects;

import java.math.BigDecimal;

public class Deposit implements DepositOperation {

	private final Long id;
	private final String status;
	private final String code;
	private final String account;
	private final BigDecimal amount;

	public Deposit(Long id, String status, String code, String account, BigDecimal amount) {
		this.id = id;
		this.status = status;
		this.code = code;
		this.account = account;
		this.amount = amount;
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
	public String getAccount() {
		return account;
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Deposit deposit = (Deposit) o;
		return Objects.equal(id, deposit.id) &&
			Objects.equal(status, deposit.status) &&
			Objects.equal(code, deposit.code) &&
			Objects.equal(account, deposit.account) &&
			Objects.equal(amount, deposit.amount);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, status, code, account, amount);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("id", id)
			.add("status", status)
			.add("code", code)
			.add("account", account)
			.add("amount", amount)
			.toString();
	}
}