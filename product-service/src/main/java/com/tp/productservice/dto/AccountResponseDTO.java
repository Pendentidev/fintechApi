package com.tp.productservice.dto;

import com.tp.productservice.account.Currency;

import java.time.LocalDateTime;

public record AccountResponseDTO(
        Long accountId,
        Long customerId,
        String accountNumber,
        Currency currency,
        Double balance,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
}
