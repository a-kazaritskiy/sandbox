package com.example.sandbox.domain.transfer;

import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.domain.fee.FeeService;
import com.example.sandbox.domain.rate.RateService;
import com.example.sandbox.storage.DataStorage;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.Currency;

class TransferServiceImpl implements TransferService {

	private final DataStorage dataStorage;
	private final RateService rateService;
	private final FeeService feeService;

	TransferServiceImpl(DataStorage dataStorage, RateService rateService, FeeService feeService) {
		this.dataStorage = dataStorage;
		this.rateService = rateService;
		this.feeService = feeService;
	}

	@Override
	public Transfer calculate(
		TransferInfo transfer
	) throws DataAccessException, AccountNotFoundException {
		String fromAccount = transfer.getFromAccount();
		Currency fromCurrency = dataStorage.getAccountCurrency(fromAccount);

		String toAccount = transfer.getToAccount();
		Currency toCurrency = dataStorage.getAccountCurrency(toAccount);

		BigDecimal toAmount = transfer.getToAmount();

		BigDecimal rate = rateService.getRate(fromCurrency, toCurrency, toAmount);
		BigDecimal fee = feeService.getFee(fromAccount, toAccount, toAmount);

		BigDecimal fromAmount = calculateFromAmount(toAmount, rate, fee);

		return new TransferImpl(
			fromAccount,
			fromCurrency,
			fromAmount,
			toAccount,
			toCurrency,
			toAmount,
			rate,
			fee
		);
	}

	@Override
	public TransferOperation make(
		TransferInfo transfer
	) throws DataAccessException, AccountNotFoundException {
		return dataStorage.transfer(calculate(transfer));
	}

	private BigDecimal calculateFromAmount(BigDecimal toAmount, BigDecimal rate, BigDecimal fee) {
		return toAmount.multiply(rate).add(fee);
	}
}