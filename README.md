# Client API

REST API built with Spring Boot for managing clients and their bank accounts, with real-time USD exchange rate integration.

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Data JPA** — persistence layer
- **MySQL** — production database
- **H2** — in-memory database for tests
- **Lombok** — boilerplate reduction
- **SpringDoc OpenAPI** — auto-generated API docs

## Getting Started

### Prerequisites

- Java 21
- MySQL running on `localhost:3306`

### Database setup

```sql
CREATE DATABASE utn;
```

### Run

```bash
./mvnw spring-boot:run
```

The API will start on `http://localhost:8080`.

Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

## API Reference

### Clients — `/clients`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/clients` | Create a client |
| `GET` | `/clients` | List all clients |
| `GET` | `/clients/{id}` | Get client by ID |
| `PUT` | `/clients/{id}` | Update a client |
| `DELETE` | `/clients/{id}` | Delete a client |

**Client fields**

| Field | Type | Values |
|-------|------|--------|
| `name` | String | |
| `surnameOrLegalName` | String | |
| `documentType` | Enum | `DNI`, `CUIT` |
| `documentNumber` | String | |
| `address` | String | |
| `phoneNumber` | String | |
| `email` | String | |
| `clientType` | Enum | `INDIVIDUAL`, `COMPANY` |
| `registrationDate` | Date | `YYYY-MM-DD` |
| `active` | Boolean | |

### Accounts — `/accounts`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/accounts` | Create an account |
| `GET` | `/accounts` | List all accounts |
| `GET` | `/accounts/{id}` | Get account by ID |
| `PUT` | `/accounts/{id}` | Update an account |
| `DELETE` | `/accounts/{id}` | Delete an account |

**Account fields**

| Field | Type | Values |
|-------|------|--------|
| `client` | Object | `{ "clientId": 1 }` |
| `accountNumber` | String | |
| `currency` | Enum | `ARS`, `USD` |
| `balance` | Number | |
| `active` | Boolean | |

> USD accounts have their balance converted to ARS using the official exchange rate at query time.

### Exchange Rates — `/exchange-rates`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/exchange-rates/usd` | Get current official USD rate |

Exchange rate data is fetched from [dolarapi.com](https://dolarapi.com). If the external API is unavailable, a fallback rate of `1500.0` is returned.

## Running Tests

```bash
./mvnw test
```

Tests are split into unit tests (Mockito, no Spring context) and controller tests (MockMvc with standaloneSetup).
