package com.tp.customerservice.dto;

import com.tp.customerservice.customer.CustomerType;
import com.tp.customerservice.customer.DocumentType;

import java.time.LocalDate;

public record CustomerRequestDTO(
        String name,
        String surnameOrLegalName,
        DocumentType documentType,
        String documentNumber,
        String address,
        String phoneNumber,
        String email,
        CustomerType customerType,
        LocalDate registrationDate,
        Boolean active
) {
}
