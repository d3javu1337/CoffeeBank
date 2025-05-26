package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.requests.transaction.PaymentNumberRequest;
import org.d3javu.backend.dto.requests.transaction.TransferByPhoneNumberRequest;
import org.d3javu.backend.service.TransactionService;
import org.d3javu.backend.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/transaction")
@RequiredArgsConstructor
@Slf4j
@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final SecurityUtil securityUtil;

    @PostMapping
    public ResponseEntity<?> transferByPhoneNumber(@RequestBody TransferByPhoneNumberRequest request) {
        if (request == null || request.phoneNumber() == null || !request.phoneNumber().matches("\\+7\\d{10}")) {
            return new ResponseEntity<>("request==null || phoneNumber==null || phoneNumber !matches as phone number",
                    HttpStatus.BAD_REQUEST);
        }
        if (request.money() <=0 ) return new ResponseEntity<>("money<=0", HttpStatus.BAD_REQUEST);
        if(this.transactionService.transferByPhoneNumber(request.phoneNumber(),
                this.securityUtil.getClientId(), request.money())){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> paymentInvoiceByNumber(@RequestBody PaymentNumberRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("resource is building rn");
    }

}
