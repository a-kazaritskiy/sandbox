package com.example.sandbox.domain.account;

public class AccountNotFoundException extends RuntimeException {

	private String id;

	public AccountNotFoundException(String id) {
		super(String.format("Account not found: %s", id));
		this.id = id;
	}

	public String getId() {
		return id;
	}
}