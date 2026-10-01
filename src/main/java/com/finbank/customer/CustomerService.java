package com.finbank.customer;

import com.finbank.shared.ConflictException;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
	private final CustomerRepository customers;

	public CustomerService(CustomerRepository customers) {
		this.customers = customers;
	}

	@Transactional
	public Customer create(String firstName, String lastName, String email) {
		String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
		if (customers.existsByEmail(normalizedEmail)) {
			throw new ConflictException("A customer with this email already exists");
		}
		return customers.save(new Customer(firstName.trim(), lastName.trim(), normalizedEmail));
	}
}
