# eureka-server

Servidor de registro y descubrimiento de servicios (Netflix Eureka). `customer-service` y `product-service` se registran acá al arrancar, y `customer-service` lo usa (vía Feign + Spring Cloud LoadBalancer) para resolver la dirección de `product-service` sin hardcodear host/puerto.

## Puerto

`8761`

## Cómo levantarlo

```bash
./mvnw spring-boot:run
```

No depende de ningún otro servicio — debe ser el primero (junto con `config-server`) en arrancar.

## Verificación

Abrir `http://localhost:8761` en el navegador: el dashboard debe listar `CUSTOMER-SERVICE` y `PRODUCT-SERVICE` como `UP` una vez que ambos estén corriendo.

## Configuración relevante

`register-with-eureka: false` y `fetch-registry: false` en `application.yml` — evita que el propio servidor intente registrarse contra sí mismo.