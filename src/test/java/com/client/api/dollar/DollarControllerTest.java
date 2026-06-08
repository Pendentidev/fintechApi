package com.client.api.dollar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DollarControllerTest {

    @Mock
    private DollarService dollarService;

    @InjectMocks
    private DollarController dollarController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dollarController).build();
    }

    @Test
    void shouldReturnOfficialDollarRate() throws Exception {
        // Given
        Dollar dollar = Dollar.builder()
                .compra(1050.0)
                .venta(1100.0)
                .casa("oficial")
                .nombre("Oficial")
                .moneda("USD")
                .fechaActualizacion("2024-01-01T00:00:00.000Z")
                .build();
        when(dollarService.getOfficialDollar()).thenReturn(dollar);

        // When / Then
        mockMvc.perform(get("/exchange-rates/usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.compra").value(1050.0))
                .andExpect(jsonPath("$.venta").value(1100.0))
                .andExpect(jsonPath("$.casa").value("oficial"))
                .andExpect(jsonPath("$.nombre").value("Oficial"))
                .andExpect(jsonPath("$.moneda").value("USD"))
                .andExpect(jsonPath("$.fechaActualizacion").value("2024-01-01T00:00:00.000Z"));
    }

    @Test
    void shouldReturnFallbackValuesWhenApiFails() throws Exception {
        // Given
        Dollar fallback = Dollar.builder()
                .compra(1500.0)
                .venta(1500.0)
                .build();
        when(dollarService.getOfficialDollar()).thenReturn(fallback);

        // When / Then
        mockMvc.perform(get("/exchange-rates/usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.compra").value(1500.0))
                .andExpect(jsonPath("$.venta").value(1500.0));
    }
}
