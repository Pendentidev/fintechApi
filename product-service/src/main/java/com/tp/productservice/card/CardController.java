package com.tp.productservice.card;

import com.tp.productservice.dto.CardRequestDTO;
import com.tp.productservice.dto.CardResponseDTO;
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
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(@RequestBody CardRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDTO> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.findById(id));
    }

    @GetMapping
    public List<CardResponseDTO> getCards() {
        return cardService.findAll();
    }

    @GetMapping("/account/{accountId}")
    public List<CardResponseDTO> getCardsByAccount(@PathVariable Long accountId) {
        return cardService.findByAccountId(accountId);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<CardResponseDTO> blockCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.block(id));
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<CardResponseDTO> unblockCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.unblock(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardById(@PathVariable Long id) {
        cardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
