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
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer() {
        // Given
        CustomerRequestDTO request = new CustomerRequestDTO("Juan", null, null, null, null, null, null, null, null, null);
        Customer entity = new Customer();
        entity.setName("Juan");
        Customer saved = new Customer();
        saved.setCustomerId(1L);
        saved.setName("Juan");
        CustomerResponseDTO response = new CustomerResponseDTO(1L, "Juan", null, null, null, null, null, null, null, null, null);

        when(customerMapper.toEntity(request)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(saved);
        when(customerMapper.toResponse(saved)).thenReturn(response);

        // When
        CustomerResponseDTO result = customerService.create(request);

        // Then
        assertEquals(response, result);
        verify(customerRepository, times(1)).save(entity);
    }

    @Test
    void shouldFindCustomerById() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        CustomerResponseDTO response = new CustomerResponseDTO(1L, "Juan", null, null, null, null, null, null, null, null, null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponse(customer)).thenReturn(response);

        // When
        CustomerResponseDTO result = customerService.findById(1L);

        // Then
        assertEquals(response, result);
    }

    @Test
    void shouldThrowWhenCustomerNotFound() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> customerService.findById(1L));
    }

    @Test
    void shouldReturnAllCustomers() {
        // Given
        List<Customer> customers = List.of(new Customer(), new Customer());
        when(customerRepository.findAll()).thenReturn(customers);

        // When
        List<CustomerResponseDTO> result = customerService.findAll();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldDeleteCustomerById() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // When
        customerService.deleteById(1L);

        // Then
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnCustomerWithProducts() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        List<ProductDTO> products = List.of(new ProductDTO(10L, "ACCOUNT", "Cuenta ARS - 001-1", 5000.0, "ARS", true));
        CustomerResponseDTO customerResponse = new CustomerResponseDTO(1L, "Juan", null, null, null, null, null, null, null, null, null);
        CustomerWithProductsResponseDTO expected = new CustomerWithProductsResponseDTO(customerResponse, products);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productClient.getProductsByCustomer(1L)).thenReturn(products);
        when(customerMapper.toResponseWithProducts(customer, products)).thenReturn(expected);

        // When
        CustomerWithProductsResponseDTO result = customerService.getCustomerWithProducts(1L);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowProductServiceUnavailableWhenFeignFails() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Request request = Request.create(Request.HttpMethod.GET, "/products/customer/1", java.util.Map.of(), null, new RequestTemplate());
        when(productClient.getProductsByCustomer(1L))
                .thenThrow(new FeignException.ServiceUnavailable("down", request, null, null));

        // When / Then
        assertThrows(ProductServiceUnavailableException.class, () -> customerService.getCustomerWithProducts(1L));
    }
}
