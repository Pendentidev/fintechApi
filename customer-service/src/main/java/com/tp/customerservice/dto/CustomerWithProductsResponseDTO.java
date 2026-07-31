package com.tp.customerservice.dto;

import java.util.List;

public record CustomerWithProductsResponseDTO(
        CustomerResponseDTO customer,
        List<ProductDTO> products
) {
}
