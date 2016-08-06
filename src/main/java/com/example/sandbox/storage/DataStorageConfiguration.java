package com.example.sandbox.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataStorageConfiguration {

	@Bean
	public OperationIdGenerator idGenerator() {
		return new OperationIdGenerator();
	}

	@Bean
	public DataStorage dataRepository(OperationIdGenerator idGenerator, InMemoryDataStorageInitializer initializer) {
		return new InMemoryDataStorage(idGenerator, initializer);
	}

	@Bean
	public InMemoryDataStorageInitializer initializer() {
		return new InMemoryDataStorageInitializer();
	}
}