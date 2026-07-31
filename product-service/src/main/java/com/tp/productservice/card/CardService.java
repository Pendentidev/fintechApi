package com.tp.productservice.card;

import com.tp.productservice.account.Account;
import com.tp.productservice.account.AccountService;
import com.tp.productservice.dto.CardRequestDTO;
import com.tp.productservice.dto.CardResponseDTO;
import com.tp.productservice.exception.InvalidCardRequestException;
import com.tp.productservice.exception.ResourceNotFoundException;
import com.tp.productservice.mapper.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardMapper cardMapper;

    public CardResponseDTO create(CardRequestDTO dto) {
        if (dto.cardType() == CardType.CREDIT && dto.creditLimit() == null) {
            throw new InvalidCardRequestException("Una tarjeta CREDIT requiere un creditLimit");
        }
        Account account = accountService.getOrThrow(dto.accountId());
        Card card = cardMapper.toEntity(dto, account);
        Card saved = cardRepository.save(card);
        return cardMapper.toResponse(saved);
    }

    public CardResponseDTO findById(Long id) {
        return cardMapper.toResponse(getOrThrow(id));
    }

    public List<CardResponseDTO> findAll() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    public List<CardResponseDTO> findByAccountId(Long accountId) {
        return cardRepository.findByAccount_AccountId(accountId).stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    public CardResponseDTO block(Long id) {
        Card card = getOrThrow(id);
        card.setStatus(CardStatus.BLOCKED);
        return cardMapper.toResponse(cardRepository.save(card));
    }

    public CardResponseDTO unblock(Long id) {
        Card card = getOrThrow(id);
        card.setStatus(CardStatus.ACTIVE);
        return cardMapper.toResponse(cardRepository.save(card));
    }

    public void deleteById(Long id) {
        getOrThrow(id);
        cardRepository.deleteById(id);
    }

    private Card getOrThrow(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
    }
}
