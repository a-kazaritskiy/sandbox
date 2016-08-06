package com.example.sandbox.storage;

import com.example.sandbox.domain.account.Account;
import com.example.sandbox.domain.account.AccountImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class InMemoryDataStorageInitializerTest {

	@InjectMocks
	private InMemoryDataStorageInitializer initializer;

	@Mock
	private Environment env;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getDefaultAccounts() throws Exception {
		when(env.getProperty(anyString(), anyString())).thenAnswer(new ReturnsArgumentAt(1));

		Map<Account, BigDecimal> accounts = ImmutableMap.of(
			new AccountImpl("system", Currency.getInstance("RUB")), new BigDecimal("100500.00"),
			new AccountImpl("me", Currency.getInstance("RUB")), new BigDecimal("100.00"),
			new AccountImpl("you", Currency.getInstance("RUB")), new BigDecimal("100.00")
		);
		assertEquals(normalizeMap(accounts), normalizeMap(initializer.getAccounts()));
	}

	@Test
	public void getConfiguredAccounts() throws Exception {
		when(env.getProperty(eq("storage.initializer.accounts"), anyString())).thenReturn("mee,youu");
		when(env.getProperty(eq("storage.initializer.account.system.currency"), anyString())).thenReturn("RUB");
		when(env.getProperty(eq("storage.initializer.account.system.balance"), anyString())).thenReturn("100500.01");
		when(env.getProperty(eq("storage.initializer.account.mee.currency"), anyString())).thenReturn("USD");
		when(env.getProperty(eq("storage.initializer.account.mee.balance"), anyString())).thenReturn("100.01");
		when(env.getProperty(eq("storage.initializer.account.youu.currency"), anyString())).thenReturn("EUR");
		when(env.getProperty(eq("storage.initializer.account.youu.balance"), anyString())).thenReturn("100.02");

		Map<Account, BigDecimal> accounts = ImmutableMap.of(
			new AccountImpl("system", Currency.getInstance("RUB")), new BigDecimal("100500.01"),
			new AccountImpl("mee", Currency.getInstance("USD")), new BigDecimal("100.01"),
			new AccountImpl("youu", Currency.getInstance("EUR")), new BigDecimal("100.02")
		);
		assertEquals(normalizeMap(accounts), normalizeMap(initializer.getAccounts()));
	}

	private Map<Account, BigDecimal> normalizeMap(Map<Account, BigDecimal> accounts) {
		return Maps.newHashMap(accounts);
	}
}