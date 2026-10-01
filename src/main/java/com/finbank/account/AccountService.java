package com.finbank.account;

import com.finbank.customer.Customer;
import com.finbank.customer.CustomerRepository;
import com.finbank.shared.NotFoundException;
import com.finbank.shared.BadRequestException;
import java.security.SecureRandom;
import java.util.Currency;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
	private static final SecureRandom RANDOM = new SecureRandom();
	private final AccountRepository accounts;
	private final CustomerRepository customers;

	public AccountService(AccountRepository accounts, CustomerRepository customers) {
		this.accounts = accounts;
		this.customers = customers;
	}

	@Transactional
	public Account create(UUID customerId, String currencyCode) {
		Customer customer = customers.findById(customerId)
				.orElseThrow(() -> new NotFoundException("Customer not found"));
		String currency;
		try {
			currency = Currency.getInstance(currencyCode.trim().toUpperCase()).getCurrencyCode();
		} catch (IllegalArgumentException exception) {
			throw new BadRequestException("Unsupported currency code");
		}
		String accountNumber;
		do {
			accountNumber = nextAccountNumber();
		} while (accounts.existsByAccountNumber(accountNumber));
		return accounts.save(new Account(accountNumber, customer, currency));
	}

	private String nextAccountNumber() {
		StringBuilder value = new StringBuilder("10");
		for (int index = 0; index < 14; index++) {
			value.append(RANDOM.nextInt(10));
		}
		return value.toString();
	}
}
