package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.requests.transaction.InvoiceNumberRequest;
import org.d3javu.backend.dto.requests.transaction.TransferByPhoneNumberRequest;
import org.d3javu.backend.service.PaymentService;
import org.d3javu.backend.service.TransactionService;
import org.d3javu.backend.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/transaction")
@RequiredArgsConstructor
@Slf4j
@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final PaymentService paymentService;
    private final SecurityUtil securityUtil;
    private final String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    @PostMapping("/transfer")
    public ResponseEntity<?> transferByPhoneNumber(@RequestBody TransferByPhoneNumberRequest request) {
        if (request == null || request.phoneNumber() == null || !request.phoneNumber().matches("\\d{10}")) {
            return new ResponseEntity<>("request==null || phoneNumber==null || phoneNumber !matches as phone number",
                    HttpStatus.BAD_REQUEST);
        }
        if (request.amount() <= 0) return new ResponseEntity<>("amount<=0", HttpStatus.BAD_REQUEST);
        if (this.transactionService.transferByPhoneNumber(request.phoneNumber(),
                this.securityUtil.getClientAccountId(), request.amount())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> InvoicePaymentByNumber(@RequestBody InvoiceNumberRequest request) {
        if (request == null || request.invoiceNumber() == null || !request.invoiceNumber().matches(uuidRegex))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (this.paymentService.purchase(request.invoiceNumber(), this.securityUtil.getClientAccountId())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/invoiceinfo")
    public ResponseEntity<?> getInvoiceInfoByNumber(@RequestParam(value = "invoiceNumber") String invoiceNumber) {
        var invoice = this.paymentService.getInvoiceInfo(invoiceNumber);
        if(invoice.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(invoice.get());
    }
}
