package com.tp.productservice.mapper;

import com.tp.productservice.account.Account;
import com.tp.productservice.dto.AccountRequestDTO;
import com.tp.productservice.dto.AccountResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toEntity(AccountRequestDTO dto) {
        Account account = new Account();
        account.setCustomerId(dto.customerId());
        account.setAccountNumber(dto.accountNumber());
        account.setCurrency(dto.currency());
        account.setBalance(dto.balance());
        account.setActive(dto.active());
        return account;
    }

    public AccountResponseDTO toResponse(Account account, Double dollarValue) {
        return new AccountResponseDTO(
                account.getAccountId(),
                account.getCustomerId(),
                account.getAccountNumber(),
                account.getCurrency(),
                account.calculateBalance(dollarValue),
                account.getActive(),
                account.getCreatedAt(),
                account.getLastUpdatedAt()
        );
    }
}
