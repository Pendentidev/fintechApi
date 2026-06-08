package com.client.api.accounts;

import com.client.api.clients.Client;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

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