package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.requests.transaction.InvoiceNumberRequest;
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

    @PostMapping("/transfer")
    public ResponseEntity<?> transferByPhoneNumber(@RequestBody TransferByPhoneNumberRequest request) {
        if (request == null || request.phoneNumber() == null || !request.phoneNumber().matches("\\+7\\d{10}")) {
            return new ResponseEntity<>("request==null || phoneNumber==null || phoneNumber !matches as phone number",
                    HttpStatus.BAD_REQUEST);
        }
        if (request.money() <= 0) return new ResponseEntity<>("money<=0", HttpStatus.BAD_REQUEST);
        if (this.transactionService.transferByPhoneNumber(request.phoneNumber(),
                this.securityUtil.getClientAccountId(), request.money())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> InvoicePaymentByNumber(@RequestBody InvoiceNumberRequest request) {
        if (request == null || request.invoiceNumber() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (this.transactionService.purchase(request.invoiceNumber().toString(), this.securityUtil.getClientAccountId())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
