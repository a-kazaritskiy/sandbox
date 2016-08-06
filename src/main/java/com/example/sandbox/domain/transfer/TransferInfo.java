package com.example.sandbox.domain.transfer;

import java.math.BigDecimal;

public interface TransferInfo {

	String getFromAccount();

	String getToAccount();

	BigDecimal getToAmount();
}