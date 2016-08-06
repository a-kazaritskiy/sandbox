package com.example.sandbox.storage;

import com.example.sandbox.domain.account.AccountImpl;
import com.example.sandbox.domain.operation.Codes;
import com.example.sandbox.domain.operation.Statuses;
import com.example.sandbox.domain.transfer.Transfer;
import com.example.sandbox.domain.transfer.TransferImpl;
import com.example.sandbox.domain.transfer.TransferOperation;
import com.example.sandbox.domain.transfer.TransferOperationImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(InMemoryDataStorage.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class InMemoryDataStorageTest {

	@Autowired
	private DataStorage dataStorage;

	@Autowired
	private OperationIdGenerator idGenerator;

	@Test
	public void findAccount() throws Exception {
		assertEquals(new AccountImpl(FROM_ACCOUNT, FROM_CURRENCY), dataStorage.findAccount(FROM_ACCOUNT));
		assertEquals(new AccountImpl(TO_ACCOUNT, TO_CURRENCY), dataStorage.findAccount(TO_ACCOUNT));
	}

	@Test
	public void getAccountCurrency() throws Exception {
		assertEquals(FROM_CURRENCY, dataStorage.getAccountCurrency(FROM_ACCOUNT));
		assertEquals(TO_CURRENCY, dataStorage.getAccountCurrency(TO_ACCOUNT));
	}

	@Test
	public void getBalance() throws Exception {
		assertEquals(FROM_ACCOUNT_BALANCE, dataStorage.getBalance(FROM_ACCOUNT));
		assertEquals(TO_ACCOUNT_BALANCE, dataStorage.getBalance(TO_ACCOUNT));
	}

	@Test
	public void transferSuccess() throws Exception {
		Transfer transfer = createTransfer(new BigDecimal("1.00"));

		long operationId = 1000L;
		when(idGenerator.getNextId()).thenReturn(operationId);

		TransferOperation expected = new TransferOperationImpl(
			operationId,
			Statuses.SUCCESS.getKey(),
			null,
			transfer
		);

		TransferOperation operation = dataStorage.transfer(transfer);
		assertEquals(expected, operation);

		assertEquals(FROM_ACCOUNT_BALANCE.subtract(transfer.getFromAmount()), dataStorage.getBalance(FROM_ACCOUNT));
		assertEquals(TO_ACCOUNT_BALANCE.add(transfer.getToAmount()), dataStorage.getBalance(TO_ACCOUNT));
	}

	@Test
	public void transferFail_InsufficientFunds() throws Exception {
		Transfer transfer = createTransfer(FROM_ACCOUNT_BALANCE.add(new BigDecimal("0.01")));

		long operationId = 1000L;
		when(idGenerator.getNextId()).thenReturn(operationId);

		TransferOperation expected = new TransferOperationImpl(
			operationId,
			Statuses.ERROR.getKey(),
			Codes.INSUFFICIENT_FUNDS.getKey(),
			transfer
		);

		TransferOperation operation = dataStorage.transfer(transfer);
		assertEquals(expected, operation);

		assertEquals(FROM_ACCOUNT_BALANCE, dataStorage.getBalance(FROM_ACCOUNT));
		assertEquals(TO_ACCOUNT_BALANCE, dataStorage.getBalance(TO_ACCOUNT));
	}

	@Test
	public void transferIntegrity() throws Exception {
		int partCount = 2;

		BigDecimal amount = FROM_ACCOUNT_BALANCE.divide(BigDecimal.valueOf(partCount), BigDecimal.ROUND_HALF_EVEN);
		Transfer transfer = createTransfer(amount);

		ExecutorService executorService = Executors.newFixedThreadPool(partCount);
		CompletionService<TransferOperation> completionService = new ExecutorCompletionService<>(executorService);

		int tryCount = partCount + 1;
		for (int i = 0; i < tryCount; i++) {
			completionService.submit(() -> dataStorage.transfer(transfer));
		}

		List<TransferOperation> operations = Lists.newLinkedList();

		for (int i = 0; i < tryCount; i++) {
			Future<TransferOperation> operationFuture = completionService.take();
			try {
				TransferOperation operation = operationFuture.get();
				operations.add(operation);
			} catch (InterruptedException | ExecutionException ignored) {
			}
		}

		List<TransferOperation> successOperations = operations.stream()
			.filter(operation -> Statuses.SUCCESS.getKey().equals(operation.getStatus()))
			.collect(Collectors.toList());
		assertEquals(partCount, successOperations.size());

		List<TransferOperation> failedOperations = operations.stream()
			.filter(operation -> Statuses.ERROR.getKey().equals(operation.getStatus()))
			.collect(Collectors.toList());
		assertEquals(tryCount - partCount, failedOperations.size());

		BigDecimal withdrawed = successOperations.stream()
			.map(TransferOperation::getFromAmount)
			.reduce(BigDecimal::add)
			.orElse(BigDecimal.ZERO);
		assertEquals(FROM_ACCOUNT_BALANCE.subtract(withdrawed), dataStorage.getBalance(FROM_ACCOUNT));

		BigDecimal deposited = successOperations.stream()
			.map(TransferOperation::getToAmount)
			.reduce(BigDecimal::add)
			.orElse(BigDecimal.ZERO);
		assertEquals(TO_ACCOUNT_BALANCE.add(deposited), dataStorage.getBalance(TO_ACCOUNT));
	}

	private static Transfer createTransfer(BigDecimal amount) {
		return new TransferImpl(
			FROM_ACCOUNT,
			FROM_CURRENCY,
			amount,
			TO_ACCOUNT,
			TO_CURRENCY,
			amount,
			BigDecimal.ONE,
			BigDecimal.ZERO
		);
	}

	@Configuration
	static class TestConfig {

		@Bean
		public OperationIdGenerator idGenerator() {
			OperationIdGenerator mock = mock(OperationIdGenerator.class);

			AtomicLong counter = new AtomicLong();
			when(mock.getNextId()).thenAnswer(invocation -> counter.incrementAndGet());

			return mock;
		}

		@Bean
		public InMemoryDataStorageInitializer initializer() {
			InMemoryDataStorageInitializer mock = mock(InMemoryDataStorageInitializer.class);
			when(mock.getAccounts()).thenReturn(ImmutableMap.of(
				new AccountImpl(FROM_ACCOUNT, FROM_CURRENCY), FROM_ACCOUNT_BALANCE,
				new AccountImpl(TO_ACCOUNT, TO_CURRENCY), TO_ACCOUNT_BALANCE
			));
			return mock;
		}
	}

	private static final String FROM_ACCOUNT = "fromAccount";
	private static final Currency FROM_CURRENCY = Currency.getInstance("RUB");
	private static final BigDecimal FROM_ACCOUNT_BALANCE = new BigDecimal("10.00");
	private static final String TO_ACCOUNT = "toAccount";
	private static final Currency TO_CURRENCY = Currency.getInstance("EUR");
	private static final BigDecimal TO_ACCOUNT_BALANCE = new BigDecimal("10.00");
}