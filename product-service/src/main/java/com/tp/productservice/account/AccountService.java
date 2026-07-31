package com.tp.productservice.account;

import com.tp.productservice.dollar.DollarService;
import com.tp.productservice.dto.AccountRequestDTO;
import com.tp.productservice.dto.AccountResponseDTO;
import com.tp.productservice.exception.ResourceNotFoundException;
import com.tp.productservice.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DollarService dollarService;

    @Autowired
    private AccountMapper accountMapper;

    public AccountResponseDTO create(AccountRequestDTO dto) {
        Account account = accountMapper.toEntity(dto);
        account.setCreatedAt(LocalDateTime.now());
        account.setLastUpdatedAt(LocalDateTime.now());
        Account saved = accountRepository.save(account);
        return accountMapper.toResponse(saved, dollarValueIfUsd(saved));
    }

    public AccountResponseDTO findById(Long id) {
        Account account = getOrThrow(id);
        return accountMapper.toResponse(account, dollarValueIfUsd(account));
    }

    public List<AccountResponseDTO> findAll() {
        List<Account> accounts = accountRepository.findAll();
        Double dollarValue = accounts.stream().anyMatch(Account::isUsd) ? dollarService.getOfficialDollar().getCompra() : null;
        return accounts.stream()
                .map(a -> accountMapper.toResponse(a, a.isUsd() ? dollarValue : null))
                .toList();
    }

    public List<AccountResponseDTO> findByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        Double dollarValue = accounts.stream().anyMatch(Account::isUsd) ? dollarService.getOfficialDollar().getCompra() : null;
        return accounts.stream()
                .map(a -> accountMapper.toResponse(a, a.isUsd() ? dollarValue : null))
                .toList();
    }

    public AccountResponseDTO update(Long id, AccountRequestDTO dto) {
        Account existing = getOrThrow(id);
        Account updated = accountMapper.toEntity(dto);
        updated.setAccountId(id);
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setLastUpdatedAt(LocalDateTime.now());
        Account saved = accountRepository.save(updated);
        return accountMapper.toResponse(saved, dollarValueIfUsd(saved));
    }

    public void deleteById(Long id) {
        getOrThrow(id);
        accountRepository.deleteById(id);
    }

    public Account getOrThrow(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    private Double dollarValueIfUsd(Account account) {
        return account.isUsd() ? dollarService.getOfficialDollar().getCompra() : null;
    }
}
