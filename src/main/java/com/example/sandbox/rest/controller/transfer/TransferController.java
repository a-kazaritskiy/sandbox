package com.example.sandbox.rest.controller.transfer;

import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.domain.account.AccountService;
import com.example.sandbox.domain.transfer.*;
import com.example.sandbox.rest.controller.ActionError;
import com.example.sandbox.rest.controller.ActionResult;
import jdk.nashorn.internal.runtime.ErrorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Currency;

@RestController
public class TransferController {

	private final AccountService accountService;
	private final TransferService transferService;

	@Autowired
	public TransferController(
		AccountService accountService,
		TransferService transferService
	) {
		this.accountService = accountService;
		this.transferService = transferService;
	}

	@RequestMapping(
		value = "/transfer/calculate",
		method = RequestMethod.POST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ActionResult<CalculatedTransferResult> calculate(
		@RequestBody @Valid TransferRequest transferRequest,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return new ActionResult<>(ActionError.of("validation", bindingResult.toString()));
		}

		try {
			Currency fromCurrency = accountService.getCurrency(transferRequest.getFromAccount());
			Currency toCurrency = accountService.getCurrency(transferRequest.getToAccount());

			Transfer transfer = transferService.calculate(info(transferRequest));
			return new ActionResult<>(new CalculatedTransferResult(
				transfer.getFromAccount(),
				fromCurrency,
				transfer.getFromAmount(),
				transfer.getToAccount(),
				toCurrency,
				transfer.getToAmount(),
				transfer.getRate(),
				transfer.getFee()
			));
		} catch (AccountNotFoundException e) {
			return new ActionResult<>(ActionError.of("account", e));
		} catch (Exception e) {
			return new ActionResult<>(ActionError.of("unknown", e));
		}
	}

	@RequestMapping(
		value = "/transfer/make",
		method = RequestMethod.POST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ActionResult<TransferResult> make(
		@RequestBody @Valid TransferRequest transferRequest,
		ErrorManager bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return new ActionResult<>(ActionError.of("validation", bindingResult.toString()));
		}

		try {
			Currency fromCurrency = accountService.getCurrency(transferRequest.getFromAccount());
			Currency toCurrency = accountService.getCurrency(transferRequest.getToAccount());

			TransferOperation transfer = transferService.make(info(transferRequest));
			if ("error".equals(transfer.getStatus())) {
				return new ActionResult<>(ActionError.of(transfer.getCode(), ""));
			}

			return new ActionResult<>(TransferResult.of(
				transfer.getId(),
				transfer.getStatus(),
				transfer.getFromAccount(),
				fromCurrency,
				transfer.getFromAmount(),
				transfer.getToAccount(),
				toCurrency,
				transfer.getToAmount(),
				transfer.getRate(),
				transfer.getFee()
			));
		} catch (AccountNotFoundException e) {
			return new ActionResult<>(ActionError.of("account", e));
		} catch (Exception e) {
			return new ActionResult<>(ActionError.of("unknown", e));
		}
	}

	private static TransferInfo info(TransferRequest transferRequest) {
		return new TransferInfoImpl(
			transferRequest.getFromAccount(),
			transferRequest.getToAccount(),
			new BigDecimal(transferRequest.getToAmount())
		);
	}
}