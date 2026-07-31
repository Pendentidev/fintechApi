package com.tp.customerservice.customer;

import com.tp.customerservice.dto.CustomerRequestDTO;
import com.tp.customerservice.dto.CustomerResponseDTO;
import com.tp.customerservice.dto.CustomerWithProductsResponseDTO;
import com.tp.customerservice.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        // Given
        CustomerResponseDTO response = new CustomerResponseDTO(1L, "Juan", null, null, null, null, null, "juan@example.com", null, null, null);
        when(customerService.create(any(CustomerRequestDTO.class))).thenReturn(response);

        // When / Then
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Juan\",\"email\":\"juan@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        // Given
        CustomerResponseDTO response = new CustomerResponseDTO(1L, "Juan", null, null, null, null, null, null, null, null, null);
        when(customerService.findById(1L)).thenReturn(response);

        // When / Then
        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        // Given
        when(customerService.findAll()).thenReturn(List.of(
                new CustomerResponseDTO(1L, "Juan", null, null, null, null, null, null, null, null, null),
                new CustomerResponseDTO(2L, "Ana", null, null, null, null, null, null, null, null, null)
        ));

        // When / Then
        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGetCustomerWithProducts() throws Exception {
        // Given
        CustomerResponseDTO customer = new CustomerResponseDTO(1L, "Juan", null, null, null, null, null, null, null, null, null);
        List<ProductDTO> products = List.of(new ProductDTO(10L, "ACCOUNT", "Cuenta ARS - 001-1", 5000.0, "ARS", true));
        when(customerService.getCustomerWithProducts(1L)).thenReturn(new CustomerWithProductsResponseDTO(customer, products));

        // When / Then
        mockMvc.perform(get("/customers/1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.customerId").value(1))
                .andExpect(jsonPath("$.products.length()").value(1));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        // Given
        CustomerResponseDTO response = new CustomerResponseDTO(1L, "Pedro", null, null, null, null, null, null, null, null, null);
        when(customerService.update(eq(1L), any(CustomerRequestDTO.class))).thenReturn(response);

        // When / Then
        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Pedro\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedro"));
    }

    @Test
    void shouldDeleteCustomerById() throws Exception {
        // When / Then
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteById(1L);
    }
}
