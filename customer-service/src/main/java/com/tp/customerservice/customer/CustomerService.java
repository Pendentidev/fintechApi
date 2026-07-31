package com.tp.customerservice.customer;

import com.tp.customerservice.client.ProductClient;
import com.tp.customerservice.dto.CustomerRequestDTO;
import com.tp.customerservice.dto.CustomerResponseDTO;
import com.tp.customerservice.dto.CustomerWithProductsResponseDTO;
import com.tp.customerservice.dto.ProductDTO;
import com.tp.customerservice.exception.ProductServiceUnavailableException;
import com.tp.customerservice.exception.ResourceNotFoundException;
import com.tp.customerservice.mapper.CustomerMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ProductClient productClient;

    public CustomerResponseDTO create(CustomerRequestDTO dto) {
        Customer saved = customerRepository.save(customerMapper.toEntity(dto));
        return customerMapper.toResponse(saved);
    }

    public CustomerResponseDTO findById(Long id) {
        return customerMapper.toResponse(getOrThrow(id));
    }

    public List<CustomerResponseDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO dto) {
        getOrThrow(id);
        Customer customer = customerMapper.toEntity(dto);
        customer.setCustomerId(id);
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    public void deleteById(Long id) {
        getOrThrow(id);
        customerRepository.deleteById(id);
    }

    public CustomerWithProductsResponseDTO getCustomerWithProducts(Long id) {
        Customer customer = getOrThrow(id);
        List<ProductDTO> products;
        try {
            products = productClient.getProductsByCustomer(id);
        } catch (FeignException ex) {
            throw new ProductServiceUnavailableException("No se pudo obtener los productos del cliente: product-service no disponible");
        }
        return customerMapper.toResponseWithProducts(customer, products);
    }

    private Customer getOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }
}
