package com.example.sandbox.domain.account;

import com.google.common.base.Objects;

import java.util.Currency;

public class AccountImpl implements Account {

	private final String id;
	private final Currency currency;

	public AccountImpl(String id, Currency currency) {
		this.id = id;
		this.currency = currency;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, currency);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final AccountImpl other = (AccountImpl) obj;
		return Objects.equal(this.id, other.id)
			&& Objects.equal(this.currency, other.currency);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("id", id)
			.add("currency", currency)
			.toString();
	}
}