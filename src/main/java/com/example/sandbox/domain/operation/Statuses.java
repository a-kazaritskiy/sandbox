package com.example.sandbox.domain.operation;

import com.example.sandbox.domain.HasKey;

public enum Statuses implements HasKey {
	PENDING("pending"),
	SUCCESS("success"),
	ERROR("error");

	private String key;

	Statuses(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}
}