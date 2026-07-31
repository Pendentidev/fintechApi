package com.tp.productservice.catalog;

import com.tp.productservice.account.Account;
import com.tp.productservice.account.AccountRepository;
import com.tp.productservice.card.Card;
import com.tp.productservice.card.CardRepository;
import com.tp.productservice.card.CardStatus;
import com.tp.productservice.card.CardType;
import com.tp.productservice.dto.ProductSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAggregatorService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    public List<ProductSummaryDTO> findByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        List<Long> accountIds = accounts.stream().map(Account::getAccountId).toList();
        List<Card> cards = accountIds.isEmpty() ? List.of() : cardRepository.findByAccount_AccountIdIn(accountIds);

        List<ProductSummaryDTO> summaries = new java.util.ArrayList<>();
        accounts.forEach(account -> summaries.add(toSummary(account)));
        cards.forEach(card -> summaries.add(toSummary(card)));
        return summaries;
    }

    private ProductSummaryDTO toSummary(Account account) {
        return new ProductSummaryDTO(
                account.getAccountId(),
                ProductType.ACCOUNT,
                "Cuenta " + account.getCurrency() + " - " + account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency().name(),
                account.getActive()
        );
    }

    private ProductSummaryDTO toSummary(Card card) {
        Double amount = card.getCardType() == CardType.CREDIT ? card.getCreditLimit() : null;
        return new ProductSummaryDTO(
                card.getCardId(),
                ProductType.CARD,
                "Tarjeta " + card.getCardType() + " - " + card.maskedNumber(),
                amount,
                null,
                card.getStatus() == CardStatus.ACTIVE
        );
    }
}
