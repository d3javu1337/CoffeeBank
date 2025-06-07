package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.requests.card.CardCreateRequest;
import org.d3javu.backend.dto.requests.card.CardRenameRequest;
import org.d3javu.backend.service.CardService;
import org.d3javu.backend.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<?> getCard(@RequestParam(value = "cardId", required = false) Long cardId) {
        if(cardId == null) return ResponseEntity.ok(this.cardService.getCardsByAccountId(this.securityUtil.getClientAccountId()));
        var card = this.cardService.getCardById(cardId);
        if (card == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unable to do this");
        return ResponseEntity.ok(card);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void openCard(@RequestBody CardCreateRequest request) {
        this.cardService.openCard(this.securityUtil.getClientEmail(), this.securityUtil.getClientAccountId(), request.type());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping
    public void updateCardName(@RequestBody CardRenameRequest request) {
        this.cardService.renameCard(this.securityUtil.getClientEmail(), this.securityUtil.getClientAccountId(), request);
    }

}
