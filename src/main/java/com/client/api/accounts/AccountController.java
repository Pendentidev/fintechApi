package com.client.api.accounts;

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
    public ResponseEntity<AccountRestModel> createAccount(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountRestModel> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @GetMapping
    public List<AccountRestModel> getAccounts() {
        return accountService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountRestModel> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return ResponseEntity.ok(accountService.update(id, account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable Long id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
