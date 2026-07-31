package com.tp.productservice.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardTest {

    @Test
    void shouldReturnTrueWhenCardTypeIsCredit() {
        // Given
        Card card = new Card();
        card.setCardType(CardType.CREDIT);

        // When / Then
        assertTrue(card.isCredit());
    }

    @Test
    void shouldReturnFalseWhenCardTypeIsDebit() {
        // Given
        Card card = new Card();
        card.setCardType(CardType.DEBIT);

        // When / Then
        assertFalse(card.isCredit());
    }

    @Test
    void shouldMaskCardNumberShowingOnlyLast4Digits() {
        // Given
        Card card = new Card();
        card.setCardNumber("4532015112830366");

        // When
        String masked = card.maskedNumber();

        // Then
        assertEquals("**** **** **** 0366", masked);
    }
}
