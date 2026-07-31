package com.tp.productservice.card;

import com.tp.productservice.account.Account;
import com.tp.productservice.account.AccountService;
import com.tp.productservice.dto.CardRequestDTO;
import com.tp.productservice.dto.CardResponseDTO;
import com.tp.productservice.exception.InvalidCardRequestException;
import com.tp.productservice.mapper.CardMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @Test
    void shouldThrowWhenCreditCardHasNoCreditLimit() {
        // Given
        CardRequestDTO dto = new CardRequestDTO(1L, "Juan Perez", CardType.CREDIT, null);

        // When / Then
        assertThrows(InvalidCardRequestException.class, () -> cardService.create(dto));
    }

    @Test
    void shouldCreateDebitCardWithoutRequiringCreditLimit() {
        // Given
        CardRequestDTO dto = new CardRequestDTO(1L, "Juan Perez", CardType.DEBIT, null);
        Account account = new Account();
        account.setAccountId(1L);
        Card card = new Card();
        CardResponseDTO response = new CardResponseDTO(1L, 1L, "Juan Perez", "**** **** **** 1234", CardType.DEBIT, CardStatus.ACTIVE, null, null);

        when(accountService.getOrThrow(1L)).thenReturn(account);
        when(cardMapper.toEntity(dto, account)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toResponse(card)).thenReturn(response);

        // When
        CardResponseDTO result = cardService.create(dto);

        // Then
        assertEquals(response, result);
    }

    @Test
    void shouldBlockCard() {
        // Given
        Card card = new Card();
        card.setCardId(1L);
        card.setStatus(CardStatus.ACTIVE);
        CardResponseDTO response = new CardResponseDTO(1L, 1L, "Juan Perez", "**** **** **** 1234", CardType.DEBIT, CardStatus.BLOCKED, null, null);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardMapper.toResponse(card)).thenReturn(response);

        // When
        CardResponseDTO result = cardService.block(1L);

        // Then
        assertEquals(CardStatus.BLOCKED, card.getStatus());
        assertEquals(response, result);
    }
}
