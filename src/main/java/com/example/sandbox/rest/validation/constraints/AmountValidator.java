package com.example.sandbox.rest.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

class AmountValidator implements ConstraintValidator<Amount, String> {

	@Override
	public void initialize(Amount constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		try {
			new BigDecimal(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}