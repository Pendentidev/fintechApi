package com.tp.productservice.catalog;

import com.tp.productservice.account.Account;
import com.tp.productservice.account.AccountRepository;
import com.tp.productservice.account.Currency;
import com.tp.productservice.card.Card;
import com.tp.productservice.card.CardRepository;
import com.tp.productservice.card.CardStatus;
import com.tp.productservice.card.CardType;
import com.tp.productservice.dto.ProductSummaryDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductAggregatorServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private ProductAggregatorService productAggregatorService;

    @Test
    void shouldMergeAccountsAndCardsForCustomer() {
        // Given
        Account account = new Account();
        account.setAccountId(1L);
        account.setCustomerId(5L);
        account.setAccountNumber("001-12345");
        account.setCurrency(Currency.ARS);
        account.setBalance(5000.0);
        account.setActive(true);

        Card card = new Card();
        card.setCardId(10L);
        card.setAccount(account);
        card.setCardType(CardType.CREDIT);
        card.setStatus(CardStatus.ACTIVE);
        card.setCreditLimit(100000.0);
        card.setCardNumber("4532015112830366");

        when(accountRepository.findByCustomerId(5L)).thenReturn(List.of(account));
        when(cardRepository.findByAccount_AccountIdIn(List.of(1L))).thenReturn(List.of(card));

        // When
        List<ProductSummaryDTO> result = productAggregatorService.findByCustomerId(5L);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.productType() == ProductType.ACCOUNT && p.amount().equals(5000.0)));
        assertTrue(result.stream().anyMatch(p -> p.productType() == ProductType.CARD && p.amount().equals(100000.0)));
    }

    @Test
    void shouldReturnEmptyListWhenCustomerHasNoAccounts() {
        // Given
        when(accountRepository.findByCustomerId(99L)).thenReturn(List.of());

        // When
        List<ProductSummaryDTO> result = productAggregatorService.findByCustomerId(99L);

        // Then
        assertEquals(0, result.size());
    }
}
