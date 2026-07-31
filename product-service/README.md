# product-service

Gestiona los productos financieros de los clientes: **cuentas** y **tarjetas** (débito/crédito). Se registra en Eureka y expone un endpoint agregador, `GET /products/customer/{customerId}`, que es el que consume `customer-service` vía Feign — devuelve una vista unificada de cuentas + tarjetas, tal como se ve en una app bancaria real.

## Puerto

`8082`

## Dependencias externas

- **MySQL** en `localhost:3306` (credenciales en `.env` local, no versionado — ver `DB_URL`/`DB_USERNAME`/`DB_PASSWORD`).
- **config-server** (`:8888`) y **eureka-server** (`:8761`) deben estar arriba antes de levantarlo.
- API de cotización del dólar ([dolarapi.com](https://dolarapi.com)) para convertir saldos en USD — si no responde, usa un valor de fallback de `1500.0`.

## Cómo levantarlo

```bash
./mvnw spring-boot:run
```

## Modelo de dominio

Una **tarjeta siempre pertenece a una cuenta** (`Card.account` es un `@ManyToOne` real hacia `Account`, con FK en la base) — así funciona un banco de verdad: la tarjeta es un medio de acceso a una cuenta, no una entidad aislada. `customerId`, en cambio, sigue siendo un `Long` plano en `Account` (no una relación JPA), porque `Customer` vive en otro microservicio con su propia base.

### Decisiones de diseño orientadas a banco

- El **número de tarjeta lo genera el servidor** al crearla; el cliente no lo envía en el request.
- Las respuestas **nunca exponen el número completo** — siempre enmascarado (`**** **** **** 1234`).
- **No se persiste CVV** — en un sistema real no se guarda después de la autorización.
- **`creditLimit` solo aplica a tarjetas `CREDIT`**: crear una `CREDIT` sin `creditLimit` devuelve `400`; en una `DEBIT` se ignora.
- La **fecha de expiración** también la fija el servidor (hoy + 5 años).
- Además del CRUD estándar, hay acciones de **bloqueo/desbloqueo** (`PUT /cards/{id}/block` / `/unblock`) — la operación más común sobre una tarjeta en la vida real.

## Endpoints

### Cuentas — `/accounts`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/accounts` | Crea una cuenta |
| `GET` | `/accounts` | Lista todas las cuentas |
| `GET` | `/accounts/{id}` | Obtiene una cuenta por ID |
| `GET` | `/accounts/customer/{customerId}` | Cuentas de un cliente |
| `PUT` | `/accounts/{id}` | Actualiza una cuenta |
| `DELETE` | `/accounts/{id}` | Elimina una cuenta |

### Tarjetas — `/cards`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/cards` | Crea una tarjeta. Body: `accountId`, `cardholderName`, `cardType` (`DEBIT`/`CREDIT`), `creditLimit` (obligatorio solo si `CREDIT`) |
| `GET` | `/cards` | Lista todas las tarjetas |
| `GET` | `/cards/{id}` | Obtiene una tarjeta por ID |
| `GET` | `/cards/account/{accountId}` | Tarjetas de una cuenta |
| `PUT` | `/cards/{id}/block` | Bloquea la tarjeta |
| `PUT` | `/cards/{id}/unblock` | Reactiva la tarjeta |
| `DELETE` | `/cards/{id}` | Elimina una tarjeta |

### Agregador — `/products` (consumido por Feign desde customer-service)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/products/customer/{customerId}` | Vista unificada: cuentas + tarjetas del cliente, como `ProductSummaryDTO` |

### Cotización — `/exchange-rates/usd`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/exchange-rates/usd` | Cotización oficial del dólar |

## Manejo de errores

`GlobalExceptionHandler` (`@RestControllerAdvice`) devuelve `{ "status": ..., "message": ... }`:
- `404` si la cuenta/tarjeta no existe.
- `400` si se intenta crear una tarjeta `CREDIT` sin `creditLimit`.
- `500` para cualquier otro error no controlado.

## Tests

```bash
./mvnw test
```

Corren contra H2 en memoria, con Eureka y Config Server deshabilitados (`src/test/resources/application.yml`) — no dependen de que el resto del ecosistema esté levantado.
