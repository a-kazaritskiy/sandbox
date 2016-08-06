package com.example.sandbox.domain.transfer;

import com.example.sandbox.domain.account.AccountNotFoundException;
import org.springframework.dao.DataAccessException;

public interface TransferService {

	Transfer calculate(TransferInfo transfer)
		throws DataAccessException, AccountNotFoundException;

	TransferOperation make(TransferInfo transfer)
		throws DataAccessException, AccountNotFoundException;
}