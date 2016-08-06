package com.example.sandbox.domain.rate;

import java.math.BigDecimal;
import java.util.Currency;

public interface RateService {

	BigDecimal getRate(Currency from, Currency to, BigDecimal amount);
}