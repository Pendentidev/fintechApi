package com.tp.productservice.catalog;

import com.tp.productservice.dto.ProductSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductAggregatorController {

    @Autowired
    private ProductAggregatorService productAggregatorService;

    @GetMapping("/customer/{customerId}")
    public List<ProductSummaryDTO> getProductsByCustomer(@PathVariable Long customerId) {
        return productAggregatorService.findByCustomerId(customerId);
    }
}
