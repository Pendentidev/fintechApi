package com.client.api.accounts;

import com.client.api.clients.ClientRepository;
import com.client.api.dollar.DollarService;
import com.client.api.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DollarService dollarService;

    public AccountRestModel create(Account account) {
        Long clientId = account.getClient().getClientId();
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client not found with id: " + clientId);
        }
        Account saved = accountRepository.save(account);
        Double dollarValue = null;
        if (saved.isUsd()) {
            dollarValue = dollarService.getOfficialDollar().getCompra();
        }
        return AccountRestModel.from(saved, dollarValue);
    }

    public AccountRestModel findById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        Double dollarValue = null;
        if (account.isUsd()) {
            dollarValue = dollarService.getOfficialDollar().getCompra();
        }
        return AccountRestModel.from(account, dollarValue);
    }

    public List<AccountRestModel> findAll() {
        List<Account> accounts = accountRepository.findAll();
        Double dollarValue;
        if (accounts.stream().anyMatch(Account::isUsd)) {
            dollarValue = dollarService.getOfficialDollar().getCompra();
        } else {
            dollarValue = null;
        }
        return accounts.stream()
                .map(a -> AccountRestModel.from(a, dollarValue))
                .toList();
    }

    public AccountRestModel update(Long id, Account account) {
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        account.setAccountId(id);
        account.setCreatedAt(existing.getCreatedAt());
        account.setLastUpdatedAt(LocalDateTime.now());
        Account saved = accountRepository.save(account);
        Double dollarRate = null;
        if (saved.isUsd()) {
            dollarRate = dollarService.getOfficialDollar().getCompra();
        }
        return AccountRestModel.from(saved, dollarRate);
    }

    public void deleteById(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }
}
