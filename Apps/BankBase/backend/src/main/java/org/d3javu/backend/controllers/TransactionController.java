package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/transaction")
@RequiredArgsConstructor
@Slf4j
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public ResponseEntity<?> transferByPhoneNumber(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("resource is building rn");
    }

    public ResponseEntity<?> transferByCardNumber(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("resource is building rn");
    }

}
