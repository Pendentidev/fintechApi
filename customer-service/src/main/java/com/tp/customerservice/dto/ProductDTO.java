package com.tp.customerservice.dto;

public record ProductDTO(
        Long productId,
        String productType,
        String description,
        Double amount,
        String currency,
        Boolean active
) {
}
