package com.tp.productservice.dto;

import com.tp.productservice.catalog.ProductType;

public record ProductSummaryDTO(
        Long productId,
        ProductType productType,
        String description,
        Double amount,
        String currency,
        Boolean active
) {
}
