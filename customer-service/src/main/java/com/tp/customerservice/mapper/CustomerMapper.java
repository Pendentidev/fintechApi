package com.tp.customerservice.mapper;

import com.tp.customerservice.customer.Customer;
import com.tp.customerservice.dto.CustomerRequestDTO;
import com.tp.customerservice.dto.CustomerResponseDTO;
import com.tp.customerservice.dto.CustomerWithProductsResponseDTO;
import com.tp.customerservice.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setSurnameOrLegalName(dto.surnameOrLegalName());
        customer.setDocumentType(dto.documentType());
        customer.setDocumentNumber(dto.documentNumber());
        customer.setAddress(dto.address());
        customer.setPhoneNumber(dto.phoneNumber());
        customer.setEmail(dto.email());
        customer.setCustomerType(dto.customerType());
        customer.setRegistrationDate(dto.registrationDate());
        customer.setActive(dto.active());
        return customer;
    }

    public CustomerResponseDTO toResponse(Customer customer) {
        return new CustomerResponseDTO(
                customer.getCustomerId(),
                customer.getName(),
                customer.getSurnameOrLegalName(),
                customer.getDocumentType(),
                customer.getDocumentNumber(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                customer.getCustomerType(),
                customer.getRegistrationDate(),
                customer.getActive()
        );
    }

    public CustomerWithProductsResponseDTO toResponseWithProducts(Customer customer, List<ProductDTO> products) {
        return new CustomerWithProductsResponseDTO(toResponse(customer), products);
    }
}
