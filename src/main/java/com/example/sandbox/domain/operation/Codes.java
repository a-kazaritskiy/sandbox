package com.example.sandbox.domain.operation;

import com.example.sandbox.domain.HasKey;

public enum Codes implements HasKey {
	INSUFFICIENT_FUNDS("insufficientFunds");

	private String key;

	Codes(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}
}