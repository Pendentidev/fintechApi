package com.tp.productservice.dollar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DollarServiceTest {

    @Mock
    private DollarApiClient dollarApiClient;

    @InjectMocks
    private DollarService dollarService;

    @Test
    void shouldReturnOfficialDollar() {
        // Given
        Dollar dollar = Dollar.builder()
                .compra(1050.0)
                .venta(1100.0)
                .casa("oficial")
                .nombre("Oficial")
                .moneda("USD")
                .fechaActualizacion("2024-01-01T00:00:00.000Z")
                .build();
        when(dollarApiClient.getOfficialDollar()).thenReturn(dollar);

        // When
        Dollar result = dollarService.getOfficialDollar();

        // Then
        assertEquals(dollar.getCompra(), result.getCompra());
        assertEquals(dollar.getVenta(), result.getVenta());
        assertEquals(dollar.getCasa(), result.getCasa());
        assertEquals(dollar.getNombre(), result.getNombre());
        assertEquals(dollar.getMoneda(), result.getMoneda());
        assertEquals(dollar.getFechaActualizacion(), result.getFechaActualizacion());
        verify(dollarApiClient, times(1)).getOfficialDollar();
    }
}
