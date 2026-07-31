package com.tp.productservice.card;

import com.tp.productservice.dto.CardRequestDTO;
import com.tp.productservice.dto.CardResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    void shouldCreateCard() throws Exception {
        // Given
        CardResponseDTO response = new CardResponseDTO(1L, 1L, "Juan Perez", "**** **** **** 1234", CardType.DEBIT, CardStatus.ACTIVE, null, null);
        when(cardService.create(any(CardRequestDTO.class))).thenReturn(response);

        // When / Then
        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\":1,\"cardholderName\":\"Juan Perez\",\"cardType\":\"DEBIT\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardId").value(1))
                .andExpect(jsonPath("$.maskedCardNumber").value("**** **** **** 1234"));
    }

    @Test
    void shouldBlockCard() throws Exception {
        // Given
        CardResponseDTO response = new CardResponseDTO(1L, 1L, "Juan Perez", "**** **** **** 1234", CardType.DEBIT, CardStatus.BLOCKED, null, null);
        when(cardService.block(1L)).thenReturn(response);

        // When / Then
        mockMvc.perform(put("/cards/1/block"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }

    @Test
    void shouldUnblockCard() throws Exception {
        // Given
        CardResponseDTO response = new CardResponseDTO(1L, 1L, "Juan Perez", "**** **** **** 1234", CardType.DEBIT, CardStatus.ACTIVE, null, null);
        when(cardService.unblock(1L)).thenReturn(response);

        // When / Then
        mockMvc.perform(put("/cards/1/unblock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
