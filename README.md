# FinBank Transaction System

A Java 21 / Spring Boot modular monolith for a banking and payment transaction API. The first milestone provides customer and bank account creation backed by PostgreSQL.

## Requirements

- Java 21
- Maven 3.9+
- Docker Compose

## Run locally

Start PostgreSQL:

```sh
docker compose up -d postgres
```

Run the API:

```sh
./mvnw spring-boot:run
```

If Maven Wrapper is not present, use `mvn spring-boot:run`.

Database settings can be overridden with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`. Flyway applies schema migrations; Hibernate validates the resulting schema.

## Initial API

Create a customer:

```http
POST /api/customers
Content-Type: application/json

{"firstName":"Ada","lastName":"Lovelace","email":"ada@example.com"}
```

Create an account using the returned customer ID:

```http
POST /api/accounts
Content-Type: application/json

{"customerId":"<customer-id>","currency":"EUR"}
```

Both endpoints return `201 Created` with a `Location` header. New accounts start with a zero balance. Duplicate email addresses return `409 Conflict`; invalid request data returns `400 Bad Request`.

## Current scope

Customer and account creation are in place, including input validation, normalized unique email addresses, currency validation, UUID identifiers, a database-enforced nonnegative balance constraint, and optimistic versioning on accounts. Authentication, balance-changing operations, transfers, and transaction history are planned next.
