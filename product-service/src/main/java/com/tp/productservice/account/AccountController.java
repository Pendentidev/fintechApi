package com.tp.productservice.account;

import com.tp.productservice.dto.AccountRequestDTO;
import com.tp.productservice.dto.AccountResponseDTO;
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
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @GetMapping
    public List<AccountResponseDTO> getAccounts() {
        return accountService.findAll();
    }

    @GetMapping("/customer/{customerId}")
    public List<AccountResponseDTO> getAccountsByCustomer(@PathVariable Long customerId) {
        return accountService.findByCustomerId(customerId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Long id, @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.ok(accountService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable Long id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
