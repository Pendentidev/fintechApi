package com.tp.productservice.dto;

import com.tp.productservice.account.Currency;

public record AccountRequestDTO(
        Long customerId,
        String accountNumber,
        Currency currency,
        Double balance,
        Boolean active
) {
}
