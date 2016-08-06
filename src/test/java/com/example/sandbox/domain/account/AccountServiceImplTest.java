package com.example.sandbox.domain.account;

import com.example.sandbox.storage.DataStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(AccountServiceImpl.class)
public class AccountServiceImplTest {

	@Autowired
	private AccountService accountService;

	@MockBean
	private DataStorage dataStorage;

	@Test
	public void find() throws Exception {
		Account expected = new AccountImpl("me", Currency.getInstance("RUB"));
		when(dataStorage.findAccount("me")).thenReturn(expected);

		Account account = accountService.find("me");
		assertEquals(expected, account);

		verify(dataStorage).findAccount("me");
	}

	@Test
	public void testGetCurrency() throws Exception {
		Currency expected = Currency.getInstance("RUB");
		when(dataStorage.getAccountCurrency("me")).thenReturn(expected);

		Currency currency = accountService.getCurrency("me");
		assertEquals(expected, currency);

		verify(dataStorage).getAccountCurrency("me");
	}
}