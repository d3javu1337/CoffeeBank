package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
//import org.d3javu.backend.grpc.PaymentServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.invoice.InvoiceDto;
import org.d3javu.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final org.d3javu.backend.grpc.TransactionServiceGrpc.TransactionServiceBlockingStub paymentServiceStub;
    private final TransactionRepository transactionRepository;

    public Boolean purchase(String invoiceNumber, Long payerId) {
        return this.paymentServiceStub.invoicePayment(
                org.d3javu.backend.grpc.InvoicePaymentRequest
                        .newBuilder()
                        .setInvoiceUUID(invoiceNumber)
                        .setPayerAccountId(payerId)
                        .build()
        ).getIsCompleted();
    }

    public Optional<InvoiceDto> getInvoiceInfo(String invoiceNumber) {
        log.info("getInvoiceInfo: " + invoiceNumber);
        var number = UUID.fromString(invoiceNumber);
        if(this.transactionRepository.existsInvoice(number) && this.transactionRepository.invoiceIsNotPayed(number))
            return Optional.of(new InvoiceDto(this.transactionRepository.getInvoiceAmount(number)));
        return Optional.empty();
    }


}
