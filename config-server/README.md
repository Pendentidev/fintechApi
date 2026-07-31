# config-server

Spring Cloud Config Server: centraliza la configuración no sensible de `customer-service` y `product-service` (puertos, URL de Eureka, datasource, JPA, URL de la API de dólar), leyéndola de un repositorio Git remoto separado.

## Puerto

`8888`

## Cómo levantarlo

```bash
./mvnw spring-boot:run
```

Debe ser el primer servicio en arrancar — los demás le piden su configuración al iniciar.

## Repositorio de configuración

`spring.cloud.config.server.git.uri` (en `application.yml`) apunta al repo **`fintech-config-repo`**, que contiene:
- `application.yml` — config común a todos los servicios (URL de Eureka).
- `customer-service.yml` — puerto, datasource y JPA de customer-service.
- `product-service.yml` — puerto, datasource, JPA y URL de la API de dólar de product-service.

## Verificación

```bash
curl http://localhost:8888/customer-service/default
curl http://localhost:8888/product-service/default
```

Cada llamada debe devolver un JSON con las propiedades correspondientes a ese servicio.