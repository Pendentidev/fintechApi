package com.tp.productservice.dto;

import com.tp.productservice.card.CardType;

public record CardRequestDTO(
        Long accountId,
        String cardholderName,
        CardType cardType,
        Double creditLimit
) {
}
