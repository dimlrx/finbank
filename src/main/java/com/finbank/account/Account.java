package com.finbank.account;

import com.finbank.customer.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "account_number", nullable = false, unique = true, length = 16)
	private String accountNumber;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal balance = BigDecimal.ZERO.setScale(2);

	@Column(nullable = false, length = 3)
	private String currency;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private AccountStatus status = AccountStatus.ACTIVE;

	@Version
	@Column(nullable = false)
	private long version;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	protected Account() {}

	public Account(String accountNumber, Customer customer, String currency) {
		this.accountNumber = accountNumber;
		this.customer = customer;
		this.currency = currency;
	}

	@jakarta.persistence.PrePersist
	void onCreate() { createdAt = Instant.now(); }

	public UUID getId() { return id; }
	public String getAccountNumber() { return accountNumber; }
	public Customer getCustomer() { return customer; }
	public BigDecimal getBalance() { return balance; }
	public String getCurrency() { return currency; }
	public AccountStatus getStatus() { return status; }
	public Instant getCreatedAt() { return createdAt; }
}
