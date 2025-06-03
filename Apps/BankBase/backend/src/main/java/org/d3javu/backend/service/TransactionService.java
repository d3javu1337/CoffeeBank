package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.model.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TransactionService {

    private final org.d3javu.backend.grpc.TransactionServiceGrpc.TransactionServiceBlockingStub transactionServiceStub;
    private final org.d3javu.backend.grpc.PaymentServiceGrpc.PaymentServiceBlockingStub paymentServiceStub;

    private final ZoneId zoneId = ZoneId.of("Europe/Moscow");

    public Boolean transferByPhoneNumber(String recipientPhoneNumber, Long senderAccountId, Double amount) {
        return this.transactionServiceStub.transferByPhoneNumber(
                org.d3javu.backend.grpc.TransferByPhoneNumberRequest.newBuilder()
                        .setSenderAccountId(senderAccountId)
                        .setRecipientPhoneNumber(recipientPhoneNumber)
                        .setAmount(amount)
                        .build()
        ).getIsCompleted();
    }

    public Boolean purchase(String invoiceNumber, Long payerId) {
        return this.paymentServiceStub.invoicePayment(
                org.d3javu.backend.grpc.InvoicePaymentRequest
                        .newBuilder()
                        .setInvoiceUUID(invoiceNumber)
                        .setPayerAccountId(payerId)
                        .build()
        ).getIsCompleted();
    }

}
