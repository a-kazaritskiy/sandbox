package com.example.sandbox.storage;

import com.example.sandbox.domain.account.Account;
import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.domain.operation.Statuses;
import com.example.sandbox.domain.transfer.Transfer;
import com.example.sandbox.domain.transfer.TransferOperation;
import com.example.sandbox.domain.transfer.TransferOperationImpl;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.dao.DataAccessException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import static com.example.sandbox.domain.operation.Codes.INSUFFICIENT_FUNDS;
import static com.example.sandbox.domain.operation.Statuses.*;

class InMemoryDataStorage implements DataStorage {

	private final OperationIdGenerator idGenerator;
	private final InMemoryDataStorageInitializer initializer;

	private Map<String, Account> accountMap = Maps.newConcurrentMap();
	private List<TransferOperation> operationEvents = Lists.newLinkedList();

	InMemoryDataStorage(OperationIdGenerator idGenerator, InMemoryDataStorageInitializer initializer) {
		this.idGenerator = idGenerator;
		this.initializer = initializer;
	}

	@PostConstruct
	void init() {
		initializer.getAccounts().forEach(this::register);
	}

	@Override
	public Account findAccount(String id) throws DataAccessException, AccountNotFoundException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Account must be set");

		Account account = accountMap.get(id);
		if (account == null) {
			throw new AccountNotFoundException(id);
		}
		return account;
	}

	@Override
	public Currency getAccountCurrency(String id) throws DataAccessException, AccountNotFoundException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Account must be set");

		return findAccount(id).getCurrency();
	}

	@Override
	public BigDecimal getBalance(String id) throws DataAccessException, AccountNotFoundException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Account must be set");

		findAccount(id);
		return operationEvents.stream()
			.filter(operation -> SUCCESS.getKey().equals(operation.getStatus()))
			.filter(operation -> id.equals(operation.getFromAccount()) || id.equals(operation.getToAccount()))
			.map(operation -> id.equals(operation.getFromAccount())
				? operation.getFromAmount().negate()
				: operation.getToAmount())
			.reduce(BigDecimal::add)
			.orElse(BigDecimal.ZERO);
	}

	@Override
	public TransferOperation transfer(
		Transfer transfer
	) throws DataAccessException, AccountNotFoundException {
		Preconditions.checkArgument(transfer != null, "TransferOperationImpl must not be null");
		Preconditions.checkArgument(transfer.getFromAccount() != null, "fromAccount must be set");
		Preconditions.checkArgument(transfer.getFromCurrency() != null, "fromCurrency must be set");
		Preconditions.checkArgument(transfer.getFromAmount() != null, "fromAmount must be set");
		Preconditions.checkArgument(transfer.getToAccount() != null, "toAccount must be set");
		Preconditions.checkArgument(transfer.getToCurrency() != null, "toCurrency must be set");
		Preconditions.checkArgument(transfer.getToAmount() != null, "toAmount must be set");
		Preconditions.checkArgument(transfer.getRate() != null, "rate must be set");
		Preconditions.checkArgument(transfer.getFee() != null, "fee must be set");

		Long operationId = idGenerator.getNextId();
		registerOperationEvent(new TransferOperationImpl(operationId, PENDING.getKey(), null, transfer));

		TransferOperation operation = doInTransaction(transfer, input -> {
			BigDecimal balance = getBalance(input.getFromAccount());
			if (balance.compareTo(input.getFromAmount()) < 0) {
				return new TransferOperationImpl(operationId, ERROR.getKey(), INSUFFICIENT_FUNDS.getKey(), transfer);
			}
			return new TransferOperationImpl(operationId, SUCCESS.getKey(), null, transfer);
		});
		registerOperationEvent(operation);
		return operation;
	}

	private void register(Account account, BigDecimal balance) {
		registerAccount(account);
		if (SYSTEM_ACCOUNT.equals(account.getId())) {
			// dirty trick (money taken from nowhere)
			registerOperationEvent(new TransferOperationImpl(
				idGenerator.getNextId(),
				Statuses.SUCCESS.getKey(),
				null,
				null,
				account.getCurrency(),
				BigDecimal.ZERO,
				account.getId(),
				account.getCurrency(),
				balance,
				BigDecimal.ONE,
				BigDecimal.ZERO
			));
		} else {
			registerOperationEvent(new TransferOperationImpl(
				idGenerator.getNextId(),
				Statuses.SUCCESS.getKey(),
				null,
				SYSTEM_ACCOUNT,
				account.getCurrency(),
				balance,
				account.getId(),
				account.getCurrency(),
				balance,
				BigDecimal.ONE,
				BigDecimal.ZERO
			));
		}
	}

	private void registerAccount(Account account) {
		accountMap.put(account.getId(), account);
	}

	private void registerOperationEvent(TransferOperation operation) {
		operationEvents.add(operation);
	}

	private TransferOperation doInTransaction(
		Transfer transfer,
		Function<Transfer, TransferOperation> function
	) {
		Lock lock = getLock(transfer.getFromAccount(), transfer.getToAccount());
		lock.lock();
		try {
			return function.apply(transfer);
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("UnusedParameters")
	private Lock getLock(String fromAccount, String toAccount) {
		// Use global lock for simplicity
		return GLOBAL_LOCK;
	}

	private static final Lock GLOBAL_LOCK = new ReentrantLock();

	private static final String SYSTEM_ACCOUNT = "system";
}