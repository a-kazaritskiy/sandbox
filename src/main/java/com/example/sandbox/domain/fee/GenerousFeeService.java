package com.example.sandbox.domain.fee;

import com.example.sandbox.domain.account.AccountNotFoundException;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;

class GenerousFeeService implements FeeService {

	@Override
	public BigDecimal getFee(
		String fromAccount,
		String toAccount,
		BigDecimal amount
	) throws DataAccessException, AccountNotFoundException {
		return BigDecimal.ZERO;
	}
}