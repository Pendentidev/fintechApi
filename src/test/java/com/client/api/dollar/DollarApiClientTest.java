package com.client.api.dollar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DollarApiClientTest {

    @Test
    void shouldReturnFallbackWhenApiIsUnavailable() {
        // Given
        DollarApiClient client = new DollarApiClient("http://localhost:1");

        // When
        Dollar result = client.getOfficialDollar();

        // Then
        assertEquals(1500.0, result.getCompra());
        assertEquals(1500.0, result.getVenta());
    }
}
