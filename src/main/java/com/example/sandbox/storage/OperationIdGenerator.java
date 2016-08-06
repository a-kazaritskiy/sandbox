package com.example.sandbox.storage;

import java.util.concurrent.atomic.AtomicLong;

class OperationIdGenerator {

	private AtomicLong operationIdCounter = new AtomicLong();

	Long getNextId() {
		return operationIdCounter.incrementAndGet();
	}
}