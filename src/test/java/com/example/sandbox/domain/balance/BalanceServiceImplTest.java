package com.example.sandbox.domain.balance;

import com.example.sandbox.storage.DataStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(BalanceServiceImpl.class)
public class BalanceServiceImplTest {

	@Autowired
	private BalanceService balanceService;

	@MockBean
	private DataStorage dataStorage;

	@Test
	public void getBalance() throws Exception {
		BigDecimal expected = BigDecimal.ONE;
		when(dataStorage.getBalance("me")).thenReturn(expected);

		BigDecimal balance = balanceService.getBalance("me");
		assertEquals(expected, balance);

		verify(dataStorage).getBalance("me");
	}
}