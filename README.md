# Fintech API — Ecosistema de Microservicios

Trabajo práctico integrador: ecosistema de microservicios con Spring Cloud (Eureka + Config Server + Feign) para la gestión de clientes y sus productos financieros (cuentas y tarjetas, con soporte a futuro para préstamos e inversiones).

## Arquitectura

```
                 ┌──────────────────────┐
                 │ fintech-config-repo  │  (repo Git remoto: application.yml,
                 │ (repo separado)      │   customer-service.yml, product-service.yml)
                 └──────────┬───────────┘
                            │ lee al arrancar
                 ┌──────────▼───────────┐
                 │    config-server     │  :8888
                 └──────────┬───────────┘
          ┌─────────────────┼─────────────────┐
          │ pide config     │                 │ pide config
   ┌──────▼──────┐   ┌──────▼───────┐   ┌──────▼───────┐
   │ eureka-server│  │ customer-    │   │ product-     │
   │    :8761     │◄─┤ service      │   │ service      │
   └─────────────┘   │   :8081      │   │   :8082      │
        ▲   ▲        └──────┬───────┘   └──────▲───────┘
        │   └───────────────┘  Feign Client     │
        │        se registran en Eureka         │
        └───────────────────────────────────────┘
```

- **`eureka-server`**: registro y descubrimiento de servicios (dashboard en `http://localhost:8761`).
- **`config-server`**: centraliza la configuración no sensible (puertos, URL de Eureka, datasource, JPA, URL de la API de dólar) leyéndola de un repositorio Git remoto separado (`fintech-config-repo`).
- **`product-service`**: gestiona los productos financieros de los clientes — **cuentas** (ARS/USD) y **tarjetas** (débito/crédito, siempre vinculadas a una cuenta). Expone `GET /products/customer/{customerId}` como agregador: junta cuentas y tarjetas en una sola vista, que es lo que consume `customer-service` vía Feign.
- **`customer-service`**: gestiona los clientes. Se comunica con `product-service` vía **Feign Client** (resolviendo la instancia a través de Eureka) para componer la vista de "cliente + sus productos".

Las contraseñas de base de datos **no** viven en el repo de configuración remoto (que puede ser público). Cada servicio las toma de un `.env` local (gitignorado) combinando `spring.config.import: "configserver:...,optional:file:.env[.properties]"` — el Config Server aporta lo no sensible, el `.env` local aporta `DB_URL`/`DB_USERNAME`/`DB_PASSWORD`.

## Orden de arranque

El orden importa: cada servicio depende de que el anterior esté disponible.

1. **`config-server`** (`:8888`) — los demás necesitan pedirle su configuración al arrancar.
2. **`eureka-server`** (`:8761`) — customer-service y product-service se registran ahí.
3. **`product-service`** (`:8082`) — customer-service lo va a llamar.
4. **`customer-service`** (`:8081`) — el último de la cadena.

## Cómo levantar cada servicio

Requiere **Java 21** y **MySQL** corriendo en `localhost:3306` (usuario/contraseña configurados en el `.env` de cada servicio — no versionado).

Desde la raíz del repo, en 4 terminales distintas:

```bash
cd config-server && ./mvnw spring-boot:run
cd eureka-server && ./mvnw spring-boot:run
cd product-service && ./mvnw spring-boot:run
cd customer-service && ./mvnw spring-boot:run
```

### Verificaciones rápidas

1. `http://localhost:8888/customer-service/default` y `http://localhost:8888/product-service/default` deben devolver la config de cada servicio.
2. `http://localhost:8761` (dashboard de Eureka) debe listar `CUSTOMER-SERVICE` y `PRODUCT-SERVICE`.
3. `curl http://localhost:8082/products/customer/1` — prueba directa a product-service (agregador de cuentas + tarjetas).
4. `curl http://localhost:8081/customers/1/products` — prueba la integración completa vía Feign + Eureka.

## Repositorio de configuración

Los archivos `.yml` que centraliza el Config Server viven en un repositorio separado: **`fintech-config-repo`**. Contiene:
- `application.yml` — configuración común a todos los servicios (URL de Eureka).
- `customer-service.yml` — puerto, datasource y JPA de customer-service.
- `product-service.yml` — puerto, datasource, JPA y URL de la API de dólar de product-service.

## Endpoints

### `customer-service` (`:8081`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/customers` | Crea un cliente |
| `GET` | `/customers` | Lista todos los clientes |
| `GET` | `/customers/{id}` | Obtiene un cliente por ID |
| `PUT` | `/customers/{id}` | Actualiza un cliente |
| `DELETE` | `/customers/{id}` | Elimina un cliente |
| `GET` | `/customers/{id}/products` | Cliente + sus productos (vía Feign a product-service) |

### `product-service` (`:8082`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST/GET/PUT/DELETE` | `/accounts`, `/accounts/{id}` | CRUD de cuentas |
| `GET` | `/accounts/customer/{customerId}` | Cuentas de un cliente |
| `POST` | `/cards` | Crea una tarjeta (débito o crédito) sobre una cuenta |
| `GET` | `/cards`, `/cards/{id}`, `/cards/account/{accountId}` | Listado / detalle de tarjetas |
| `PUT` | `/cards/{id}/block`, `/cards/{id}/unblock` | Bloquea / reactiva una tarjeta |
| `DELETE` | `/cards/{id}` | Elimina una tarjeta |
| `GET` | `/products/customer/{customerId}` | **Agregador**: cuentas + tarjetas del cliente combinadas (consumido por Feign) |
| `GET` | `/exchange-rates/usd` | Cotización oficial del dólar (usada para convertir saldos en USD) |

Detalle completo de diseño (número de tarjeta enmascarado, sin CVV, `creditLimit` solo para `CREDIT`, etc.) en [`product-service/README.md`](product-service/README.md).

## Manejo de errores

Ambos servicios exponen un `@RestControllerAdvice` (`GlobalExceptionHandler`) que traduce las excepciones a una respuesta JSON `{ "status": ..., "message": ... }`:
- Recurso no encontrado → `404`.
- En `product-service`, crear una tarjeta `CREDIT` sin `creditLimit` → `400`.
- En `customer-service`, si `product-service` no responde (Feign falla), se traduce a `503` en vez de propagar un error crudo.
- Cualquier otro error no controlado → `500`.

## Tests

```bash
cd customer-service && ./mvnw test
cd product-service && ./mvnw test
```

Los tests corren contra H2 en memoria y con Eureka/Config Server deshabilitados (`src/test/resources/application.yml` de cada servicio), por lo que no dependen de que el ecosistema esté levantado.
