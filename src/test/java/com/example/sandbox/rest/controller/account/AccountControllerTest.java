package com.example.sandbox.rest.controller.account;

import com.example.sandbox.domain.account.AccountImpl;
import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.domain.account.AccountService;
import com.example.sandbox.domain.balance.BalanceService;
import com.example.sandbox.domain.deposit.Deposit;
import com.example.sandbox.domain.deposit.DepositService;
import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Currency;

import static com.example.sandbox.domain.operation.Statuses.SUCCESS;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private BalanceService balanceService;

	@MockBean
	private DepositService depositService;

	@Test
	public void testInfoSuccess() throws Exception {
		String account = "me";
		Currency currency = Currency.getInstance("RUB");
		BigDecimal balance = new BigDecimal("1.00");

		when(accountService.find(account))
			.thenReturn(new AccountImpl(account, currency));

		when(balanceService.getBalance(account))
			.thenReturn(balance);

		mvc.perform(get("/accounts/{id}", account)
			.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().json(Resources.toString(getResource("account/info-success.json"), UTF_8)));
	}

	@Test
	public void testDepositSuccess() throws Exception {
		String account = "me";
		Currency currency = Currency.getInstance("RUB");
		BigDecimal amount = new BigDecimal("1.00");
		BigDecimal balance = new BigDecimal("2.00");

		when(accountService.getCurrency(account))
			.thenReturn(currency);

		when(balanceService.getBalance(account))
			.thenReturn(balance);

		when(depositService.make(account, amount))
			.thenReturn(new Deposit(1L, SUCCESS.getKey(), null, account, balance.add(amount)));

		mvc.perform(post("/accounts/{id}/deposit", account)
			.contentType(MediaType.APPLICATION_JSON)
			.content(Resources.toString(getResource("account/deposit-request.json"), UTF_8))
			.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().json(Resources.toString(getResource("account/deposit-result.json"), UTF_8)));
	}

	@Test
	public void testDepositFailed_NotFound() throws Exception {
		when(accountService.getCurrency("you"))
			.thenThrow(new AccountNotFoundException("you"));

		mvc.perform(post("/accounts/{id}/deposit", "you")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"currency\":\"RUB\",\"amount\":\"1.00\"}")
			.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().json(Resources.toString(getResource("account/deposit-error-account.json"), UTF_8)));
	}
}