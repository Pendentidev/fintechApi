package com.client.api.dollar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DollarApiClient {
    private final RestClient restClient;

    public DollarApiClient(@Value("${dolarapi.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Dollar getOfficialDollar() {
        try {
            return restClient.get()
                    .retrieve()
                    .body(Dollar.class);
        } catch (Exception e) {
            return Dollar.builder()
                    .compra(1500.0)
                    .venta(1500.0)
                    .build();
        }
    }
}