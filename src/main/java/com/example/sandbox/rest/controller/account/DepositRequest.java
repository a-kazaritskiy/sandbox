package com.example.sandbox.rest.controller.account;

import com.example.sandbox.rest.validation.constraints.Amount;
import org.hibernate.validator.constraints.NotBlank;

class DepositRequest {

	@NotBlank
	@Amount
	private String amount;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}