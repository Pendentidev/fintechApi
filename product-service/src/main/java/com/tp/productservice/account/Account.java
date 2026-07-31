package com.tp.productservice.account;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Long customerId;

    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Double balance;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    public boolean isUsd() {
        return currency == Currency.USD;
    }

    public Double calculateBalance(Double dollarValue) {
        return dollarValue != null ? balance * dollarValue : balance;
    }
}
