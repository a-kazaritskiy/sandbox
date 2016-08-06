package com.example.sandbox.rest.controller.account;

import com.example.sandbox.domain.account.Account;
import com.example.sandbox.domain.account.AccountNotFoundException;
import com.example.sandbox.domain.account.AccountService;
import com.example.sandbox.domain.balance.BalanceService;
import com.example.sandbox.domain.deposit.DepositOperation;
import com.example.sandbox.domain.deposit.DepositService;
import com.example.sandbox.rest.controller.ActionError;
import com.example.sandbox.rest.controller.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Currency;

@RestController
public class AccountController {

	private final AccountService accountService;
	private final BalanceService balanceService;
	private final DepositService depositService;

	@Autowired
	public AccountController(
		AccountService accountService,
		BalanceService balanceService,
		DepositService depositService
	) {
		this.accountService = accountService;
		this.balanceService = balanceService;
		this.depositService = depositService;
	}

	@RequestMapping(
		value = "/accounts/{id}",
		method = RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ActionResult<AccountResult> info(
		@PathVariable("id") String id
	) {
		try {
			Account account = accountService.find(id);
			BigDecimal balance = balanceService.getBalance(id);
			return new ActionResult<>(AccountResult.of(account.getId(), account.getCurrency(), balance));
		} catch (AccountNotFoundException e) {
			return new ActionResult<>(ActionError.of("account", e));
		} catch (Exception e) {
			return new ActionResult<>(ActionError.of("unknown", e));
		}
	}

	@RequestMapping(
		value = "/accounts/{id}/deposit",
		method = RequestMethod.POST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ActionResult<DepositResult> deposit(
		@PathVariable("id") String id,
		@RequestBody @Valid DepositRequest depositRequest,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return new ActionResult<>(ActionError.of("validation", bindingResult.toString()));
		}

		try {
			Currency currency = accountService.getCurrency(id);

			BigDecimal amount = new BigDecimal(depositRequest.getAmount());

			DepositOperation deposit = depositService.make(id, amount);

			BigDecimal balance = balanceService.getBalance(id);
			return new ActionResult<>(new DepositResult(
				String.valueOf(deposit.getId()),
				deposit.getStatus(),
				currency,
				amount,
				balance
			));
		} catch (AccountNotFoundException e) {
			return new ActionResult<>(ActionError.of("account", e));
		} catch (Exception e) {
			return new ActionResult<>(ActionError.of("unknown", e));
		}
	}
}