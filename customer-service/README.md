# customer-service

Gestiona los clientes. Se registra en Eureka y se comunica con `product-service` mediante **Feign Client**, resolviendo la instancia a través de Eureka, para componer la vista de "cliente + sus productos".

## Puerto

`8081`

## Dependencias externas

- **MySQL** en `localhost:3306` (credenciales en `.env` local, no versionado — ver `DB_URL`/`DB_USERNAME`/`DB_PASSWORD`).
- **config-server** (`:8888`) y **eureka-server** (`:8761`) deben estar arriba antes de levantarlo.
- **product-service** (`:8082`) debe estar registrado en Eureka para que `GET /customers/{id}/products` funcione (si no está disponible, responde `503` en vez de fallar con un error crudo).

## Cómo levantarlo

```bash
./mvnw spring-boot:run
```

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/customers` | Crea un cliente |
| `GET` | `/customers` | Lista todos los clientes |
| `GET` | `/customers/{id}` | Obtiene un cliente por ID |
| `PUT` | `/customers/{id}` | Actualiza un cliente |
| `DELETE` | `/customers/{id}` | Elimina un cliente |
| `GET` | `/customers/{id}/products` | Cliente + sus productos (vía Feign a product-service) |

## Integración con product-service

`ProductClient` (`com.tp.customerservice.client.ProductClient`) es la interfaz Feign:

```java
@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/products/customer/{customerId}")
    List<ProductDTO> getProductsByCustomer(@PathVariable("customerId") Long customerId);
}
```

`name = "product-service"` es el nombre lógico que Eureka resuelve a la instancia real — no hay URL hardcodeada.

## Manejo de errores

`GlobalExceptionHandler` (`@RestControllerAdvice`) devuelve `{ "status": ..., "message": ... }`: `404` si el cliente no existe, `503` si `product-service` no responde, `500` para cualquier otro error no controlado.

## Tests

```bash
./mvnw test
```

Corren contra H2 en memoria, con Eureka y Config Server deshabilitados (`src/test/resources/application.yml`) — no dependen de que el resto del ecosistema esté levantado.