package com.example.sandbox.domain.rate;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * {@code RateService} that always return 1
 */
class FairRateService implements RateService {

	@Override
	public BigDecimal getRate(Currency from, Currency to, BigDecimal amount) {
		return BigDecimal.ONE;
	}
}