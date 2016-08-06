package com.example.sandbox.rest.controller.transfer;

import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.domain.account.AccountService;
import com.example.sandbox.domain.transfer.*;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransferController.class)
public class TransferControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private TransferService transferService;

	@Test
	public void testTransferCalculateSuccess() throws Exception {
		Currency currency = Currency.getInstance("RUB");

		String fromAccount = "me";
		String toAccount = "you";
		BigDecimal toAmount = new BigDecimal("1.00");

		BigDecimal rate = new BigDecimal("1");
		BigDecimal fee = new BigDecimal("0.00");
		BigDecimal fromAmount = new BigDecimal("1.00");

		when(accountService.getCurrency(anyString()))
			.thenReturn(currency);

		when(transferService.calculate(new TransferInfoImpl(fromAccount, toAccount, toAmount)))
			.thenReturn(new TransferImpl(fromAccount, currency, fromAmount, toAccount, currency, toAmount, rate, fee));

		mvc.perform(post("/transfer/calculate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(Resources.toString(getResource("transfer/transfer-request.json"), UTF_8))
			.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().json(Resources.toString(getResource("transfer/transfer-calculate-result.json"), UTF_8)));
	}

	@Test
	public void testTransferSuccess() throws Exception {
		Currency currency = Currency.getInstance("RUB");

		String fromAccount = "me";
		String toAccount = "you";
		BigDecimal toAmount = new BigDecimal("1.00");

		BigDecimal rate = new BigDecimal("1");
		BigDecimal fee = new BigDecimal("0.00");
		BigDecimal fromAmount = new BigDecimal("1.00");

		when(accountService.getCurrency(anyString()))
			.thenReturn(currency);

		when(transferService.make(new TransferInfoImpl(fromAccount, toAccount, toAmount)))
			.thenReturn(new TransferOperationImpl(
				1L,
				SUCCESS.getKey(),
				null,
				fromAccount,
				currency,
				fromAmount,
				toAccount,
				currency,
				toAmount,
				rate,
				fee
			));

		mvc.perform(post("/transfer/make")
			.contentType(MediaType.APPLICATION_JSON)
			.content(Resources.toString(getResource("transfer/transfer-request.json"), UTF_8))
			.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().json(Resources.toString(getResource("transfer/transfer-result.json"), UTF_8)));
	}

	@Test
	public void testTransferFailed_NotFound() throws Exception {
		when(transferService.make(any(TransferInfo.class)))
			.thenThrow(new AccountNotFoundException(""));

		mvc.perform(post("/transfer/make")
			.contentType(MediaType.APPLICATION_JSON)
			.content(Resources.toString(getResource("transfer/transfer-request.json"), UTF_8))
			.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().json(Resources.toString(getResource("transfer/transfer-error-account.json"), UTF_8)));
	}
}