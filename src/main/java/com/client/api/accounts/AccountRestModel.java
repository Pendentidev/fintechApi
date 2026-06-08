package com.client.api.accounts;

import com.client.api.clients.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRestModel {

    private Long accountId;
    private Client client;
    private String accountNumber;
    private Currency currency;
    private Double balance;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    public static AccountRestModel from(Account account, Double dollarValue) {
        return AccountRestModel.builder()
                .accountId(account.getAccountId())
                .client(account.getClient())
                .accountNumber(account.getAccountNumber())
                .currency(account.getCurrency())
                .balance(account.calculateBalance(dollarValue))
                .active(account.getActive())
                .createdAt(account.getCreatedAt())
                .lastUpdatedAt(account.getLastUpdatedAt())
                .build();
    }
}