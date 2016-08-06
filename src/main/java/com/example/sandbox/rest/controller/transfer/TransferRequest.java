package com.example.sandbox.rest.controller.transfer;

import com.example.sandbox.rest.validation.constraints.Amount;
import org.hibernate.validator.constraints.NotBlank;

class TransferRequest {

	@NotBlank
	private String fromAccount;

	@NotBlank
	private String toAccount;

	@NotBlank
	@Amount
	private String toAmount;

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getToAmount() {
		return toAmount;
	}

	public void setToAmount(String toAmount) {
		this.toAmount = toAmount;
	}
}