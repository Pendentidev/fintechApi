package com.tp.productservice.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {

    @Test
    void shouldReturnTrueWhenCurrencyIsUsd() {
        // Given
        Account account = new Account();
        account.setCurrency(Currency.USD);

        // When
        boolean result = account.isUsd();

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenCurrencyIsArs() {
        // Given
        Account account = new Account();
        account.setCurrency(Currency.ARS);

        // When
        boolean result = account.isUsd();

        // Then
        assertFalse(result);
    }

    @Test
    void shouldCalculateBalanceWithDollarValue() {
        // Given
        Account account = new Account();
        account.setBalance(100.0);

        // When
        Double result = account.calculateBalance(1100.0);

        // Then
        assertEquals(110000.0, result);
    }

    @Test
    void shouldReturnOriginalBalanceWhenDollarValueIsNull() {
        // Given
        Account account = new Account();
        account.setBalance(100.0);

        // When
        Double result = account.calculateBalance(null);

        // Then
        assertEquals(100.0, result);
    }
}
