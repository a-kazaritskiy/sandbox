package com.example.sandbox.domain.deposit;

import com.example.sandbox.domain.operation.Statuses;
import com.example.sandbox.domain.transfer.Transfer;
import com.example.sandbox.domain.transfer.TransferImpl;
import com.example.sandbox.domain.transfer.TransferOperation;
import com.example.sandbox.domain.transfer.TransferOperationImpl;
import com.example.sandbox.storage.DataStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(DepositServiceImpl.class)
public class DepositServiceImplTest {

	@Autowired
	private DepositService depositService;

	@MockBean
	private DataStorage dataStorage;

	@Test
	public void make() throws Exception {
		String account = "me";
		Currency currency = Currency.getInstance("RUB");
		BigDecimal amount = BigDecimal.TEN;

		Transfer transfer = new TransferImpl(
			"system",
			currency,
			amount,
			account,
			currency,
			amount,
			BigDecimal.ONE,
			BigDecimal.ZERO
		);
		TransferOperation transferOperation = new TransferOperationImpl(
			1L,
			Statuses.SUCCESS.getKey(),
			null,
			transfer
		);

		DepositOperation expected = new Deposit(
			transferOperation.getId(),
			transferOperation.getStatus(),
			transferOperation.getCode(),
			account,
			amount
		);

		when(dataStorage.getAccountCurrency(account)).thenReturn(currency);
		when(dataStorage.transfer(transfer)).thenReturn(transferOperation);

		DepositOperation operation = depositService.make(account, amount);
		assertEquals(expected, operation);

		verify(dataStorage).getAccountCurrency(account);
		verify(dataStorage).transfer(transfer);
	}
}