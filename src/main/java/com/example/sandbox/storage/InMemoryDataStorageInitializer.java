package com.example.sandbox.storage;

import com.example.sandbox.domain.account.Account;
import com.example.sandbox.domain.account.AccountImpl;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class InMemoryDataStorageInitializer {

	@Autowired
	private Environment env;

	Map<Account, BigDecimal> getAccounts() {
		List<String> accounts = ImmutableList.<String>builder()
			.add(SYSTEM_ACCOUNT)
			.addAll(Splitter
				.on(",")
				.trimResults()
				.splitToList(env.getProperty("storage.initializer.accounts", "me,you"))
			).build();

		return accounts.stream()
			.map(account -> new AccountImpl(
				account,
				Currency.getInstance(
					env.getProperty("storage.initializer.account." + account + ".currency", DEFAULT_CURRENCY_CODE)
				))
			).collect(Collectors.toMap(Function.identity(), new Function<Account, BigDecimal>() {
				@Override
				public BigDecimal apply(Account account) {
					String defaultBalance = SYSTEM_ACCOUNT.equals(account.getId()) ? "100500.00" : "100.00";
					return new BigDecimal(
						env.getProperty("storage.initializer.account." + account.getId() + ".balance", defaultBalance)
					);
				}
			}));
	}

	private static final String SYSTEM_ACCOUNT = "system";
	private static final String DEFAULT_CURRENCY_CODE = "RUB";
}