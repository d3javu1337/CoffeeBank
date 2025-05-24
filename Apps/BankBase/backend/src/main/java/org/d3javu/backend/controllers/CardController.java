package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.requests.account.AccountIdRequest;
import org.d3javu.backend.dto.requests.card.AccountIdAndCardIdRequest;
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
    public ResponseEntity<?> getCard(@RequestParam("accountId") Long accountId, @RequestParam(value = "cardId", required = false) Long cardId) {
        if(accountId == null && cardId == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        if(cardId == null) return ResponseEntity.ok(this.cardService.getCardsByAccountId(accountId));
        var card = this.cardService.getCardById(accountId, cardId);
        if (card == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unable to so this");
        return ResponseEntity.ok(card);
    }

//    @GetMapping(params = {"AccountIdRequest"})
//    public ResponseEntity<?> getCards(@RequestBody AccountIdRequest accountId) {
//        return ResponseEntity.ok(this.cardService.getCardsByAccountId(accountId.accountId()));
//    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void createCard(@RequestBody CardCreateRequest request) {
        this.cardService.createCard(this.securityUtil.getClientEmail(), request);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping
    public void updateCardName(@RequestBody CardRenameRequest request) {
        this.cardService.renameCard(this.securityUtil.getClientEmail(), request);
    }

//    @DeleteMapping
//    public void closeCard(...) {
//        this.cardService.closeCard(...);
//    }

}
