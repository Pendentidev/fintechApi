package com.tp.productservice.dollar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Slf4j
@Component
public class DollarApiClient {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    private static final double FALLBACK_RATE = 1500.0;

    private final RestClient restClient;

    public DollarApiClient(@Value("${dolarapi.url}") String baseUrl) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(REQUEST_TIMEOUT);
        requestFactory.setReadTimeout(REQUEST_TIMEOUT);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Dollar getOfficialDollar() {
        try {
            return restClient.get()
                    .retrieve()
                    .body(Dollar.class);
        } catch (Exception e) {
            log.warn("No se pudo obtener la cotizacion del dolar, usando fallback de {}: {}", FALLBACK_RATE, e.getMessage());
            return Dollar.builder()
                    .compra(FALLBACK_RATE)
                    .venta(FALLBACK_RATE)
                    .build();
        }
    }
}
