package com.tp.customerservice.customer;

import com.tp.customerservice.dto.CustomerRequestDTO;
import com.tp.customerservice.dto.CustomerResponseDTO;
import com.tp.customerservice.dto.CustomerWithProductsResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping
    public List<CustomerResponseDTO> getCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<CustomerWithProductsResponseDTO> getCustomerWithProducts(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerWithProducts(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO dto) {
        return ResponseEntity.ok(customerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
