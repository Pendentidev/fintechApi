package com.tp.productservice.mapper;

import com.tp.productservice.account.Account;
import com.tp.productservice.card.Card;
import com.tp.productservice.card.CardStatus;
import com.tp.productservice.card.CardType;
import com.tp.productservice.dto.CardRequestDTO;
import com.tp.productservice.dto.CardResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CardMapper {

    public Card toEntity(CardRequestDTO dto, Account account) {
        Card card = new Card();
        card.setAccount(account);
        card.setCardholderName(dto.cardholderName());
        card.setCardType(dto.cardType());
        card.setCreditLimit(dto.cardType() == CardType.CREDIT ? dto.creditLimit() : null);
        card.setStatus(CardStatus.ACTIVE);
        card.setCardNumber(generateCardNumber());
        card.setExpirationDate(LocalDate.now().plusYears(5));
        card.setCreatedAt(LocalDateTime.now());
        return card;
    }

    public CardResponseDTO toResponse(Card card) {
        return new CardResponseDTO(
                card.getCardId(),
                card.getAccount().getAccountId(),
                card.getCardholderName(),
                card.maskedNumber(),
                card.getCardType(),
                card.getStatus(),
                card.getCreditLimit(),
                card.getExpirationDate()
        );
    }

    private String generateCardNumber() {
        StringBuilder number = new StringBuilder();
        number.append(ThreadLocalRandom.current().nextInt(1, 10));
        for (int i = 0; i < 15; i++) {
            number.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return number.toString();
    }
}
