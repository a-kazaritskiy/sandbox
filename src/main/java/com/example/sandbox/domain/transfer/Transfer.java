package com.example.sandbox.domain.transfer;

import java.math.BigDecimal;
import java.util.Currency;

public interface Transfer extends TransferInfo {

	Currency getFromCurrency();

	Currency getToCurrency();

	BigDecimal getRate();

	BigDecimal getFee();

	BigDecimal getFromAmount();
}