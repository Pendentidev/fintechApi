package com.tp.productservice.dto;

import com.tp.productservice.card.CardStatus;
import com.tp.productservice.card.CardType;

import java.time.LocalDate;

public record CardResponseDTO(
        Long cardId,
        Long accountId,
        String cardholderName,
        String maskedCardNumber,
        CardType cardType,
        CardStatus status,
        Double creditLimit,
        LocalDate expirationDate
) {
}
