package com.example.sandbox.domain.transfer;

import com.example.sandbox.domain.fee.FeeService;
import com.example.sandbox.domain.operation.Statuses;
import com.example.sandbox.domain.rate.RateService;
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
@Import(TransferServiceImpl.class)
public class TransferServiceImplTest {

	@Autowired
	private TransferService transferService;

	@MockBean
	private DataStorage dataStorage;

	@MockBean
	private RateService rateService;

	@MockBean
	private FeeService feeService;

	@Test
	public void calculate() throws Exception {
		String fromAccount = "me";
		Currency fromCurrency = Currency.getInstance("RUB");
		BigDecimal fromAmount = BigDecimal.ONE;

		String toAccount = "you";
		Currency toCurrency = Currency.getInstance("USD");
		BigDecimal toAmount = BigDecimal.ONE;

		BigDecimal rate = BigDecimal.ONE;
		BigDecimal fee = BigDecimal.ZERO;

		TransferInfo transferInfo = new TransferInfoImpl(
			fromAccount,
			toAccount,
			toAmount
		);

		Transfer expected = new TransferImpl(
			transferInfo,
			fromCurrency,
			fromAmount,
			toCurrency,
			rate,
			fee
		);

		when(dataStorage.getAccountCurrency(fromAccount)).thenReturn(fromCurrency);
		when(dataStorage.getAccountCurrency(toAccount)).thenReturn(toCurrency);
		when(rateService.getRate(fromCurrency, toCurrency, toAmount)).thenReturn(rate);
		when(feeService.getFee(fromAccount, toAccount, toAmount)).thenReturn(fee);

		Transfer transfer = transferService.calculate(transferInfo);
		assertEquals(expected, transfer);

		verify(dataStorage).getAccountCurrency(fromAccount);
		verify(dataStorage).getAccountCurrency(toAccount);
		verify(rateService).getRate(fromCurrency, toCurrency, toAmount);
		verify(feeService).getFee(fromAccount, toAccount, toAmount);
	}

	@Test
	public void transfer() throws Exception {
		String fromAccount = "me";
		Currency fromCurrency = Currency.getInstance("RUB");
		BigDecimal fromAmount = BigDecimal.ONE;

		String toAccount = "you";
		Currency toCurrency = Currency.getInstance("USD");
		BigDecimal toAmount = BigDecimal.ONE;

		BigDecimal rate = BigDecimal.ONE;
		BigDecimal fee = BigDecimal.ZERO;

		TransferInfo transferInfo = new TransferInfoImpl(
			fromAccount,
			toAccount,
			toAmount
		);

		Transfer transfer = new TransferImpl(
			transferInfo,
			fromCurrency,
			fromAmount,
			toCurrency,
			rate,
			fee
		);

		TransferOperation expected = new TransferOperationImpl(
			1L,
			Statuses.SUCCESS.getKey(),
			null,
			transfer
		);

		when(dataStorage.getAccountCurrency(fromAccount)).thenReturn(fromCurrency);
		when(dataStorage.getAccountCurrency(toAccount)).thenReturn(toCurrency);
		when(rateService.getRate(fromCurrency, toCurrency, toAmount)).thenReturn(rate);
		when(feeService.getFee(fromAccount, toAccount, toAmount)).thenReturn(fee);
		when(dataStorage.transfer(transfer)).thenReturn(expected);

		TransferOperation operation = transferService.make(transferInfo);
		assertEquals(expected, operation);

		verify(dataStorage).transfer(transfer);
	}
}