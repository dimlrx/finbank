package com.finbank.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@RequestMapping("/api/customers")
public class CustomerController {
	private final CustomerService service;

	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
		Customer customer = service.create(request.firstName(), request.lastName(), request.email());
		return ResponseEntity.created(URI.create("/api/customers/" + customer.getId()))
				.body(CustomerResponse.from(customer));
	}

	public record CreateCustomerRequest(
			@NotBlank @Size(max = 80) String firstName,
			@NotBlank @Size(max = 80) String lastName,
			@NotBlank @Email @Size(max = 254) String email) {}

	public record CustomerResponse(UUID id, String firstName, String lastName, String email,
			CustomerStatus status, Instant createdAt) {
		static CustomerResponse from(Customer customer) {
			return new CustomerResponse(customer.getId(), customer.getFirstName(), customer.getLastName(),
					customer.getEmail(), customer.getStatus(), customer.getCreatedAt());
		}
	}
}
