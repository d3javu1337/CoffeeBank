package org.d3javu.backend.services.business;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.d3javu.backend.grpc.*;
import org.d3javu.backend.repository.business.InvoiceRepository;
import org.d3javu.backend.repository.business.PaymentAccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService extends InvoiceServiceGrpc.InvoiceServiceImplBase {

    private final InvoiceRepository invoiceRepository;

    private final PaymentAccountRepository paymentAccountRepository;

    public Boolean existsInvoice(String invoiceIdString) {
        return invoiceRepository.existsById(UUID.fromString(invoiceIdString));
    }

    public Double getInvoiceAmount(String invoiceIdString) {
        return this.invoiceRepository.getInvoiceAmountById(UUID.fromString(invoiceIdString));
    }

    public Long getRecipientPaymentAccountId(String invoiceIdString) {
        return this.invoiceRepository.getInvoiceProviderPaymentAccountIdById(UUID.fromString(invoiceIdString));
    }

    @Override
    public void invoiceIssue(InvoiceIssueRequest request, StreamObserver<InvoiceIssueResponse> responseObserver) {
        var invoiceId = this.invoiceRepository.createInvoice(request.getProviderPaymentAccountId(), request.getAmount());
        responseObserver.onNext(InvoiceIssueResponse.newBuilder()
                .setPaymentLink(this.generateInvoicePaymentLink(invoiceId))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void invoiceIssuingTokenCreate(InvoiceIssuingTokenCreateRequest request, StreamObserver<InvoiceIssuingTokenCreateResponse> responseObserver) {
        var token = UUID.randomUUID();
        this.paymentAccountRepository.createInvoiceIssuingToken(request.getPaymentAccountId(), token);
        responseObserver.onNext(InvoiceIssuingTokenCreateResponse.newBuilder()
                .setToken(token.toString())
                .build());
        responseObserver.onCompleted();
    }

    private String generateInvoicePaymentLink(UUID invoiceId) {
        return String.format("/transaction/invoice?id=%s", invoiceId.toString());
    }

}
