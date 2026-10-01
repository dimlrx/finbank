package com.finbank.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	private final AccountService service;

	public AccountController(AccountService service) { this.service = service; }

	@PostMapping
	public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
		Account account = service.create(request.customerId(), request.currency());
		return ResponseEntity.created(URI.create("/api/accounts/" + account.getId()))
				.body(AccountResponse.from(account));
	}

	public record CreateAccountRequest(@NotNull UUID customerId,
			@NotBlank @Pattern(regexp = "[A-Za-z]{3}") String currency) {}

	public record AccountResponse(UUID id, String accountNumber, UUID customerId,
			BigDecimal balance, String currency, AccountStatus status, Instant createdAt) {
		static AccountResponse from(Account account) {
			return new AccountResponse(account.getId(), account.getAccountNumber(),
					account.getCustomer().getId(), account.getBalance(), account.getCurrency(),
					account.getStatus(), account.getCreatedAt());
		}
	}
}
