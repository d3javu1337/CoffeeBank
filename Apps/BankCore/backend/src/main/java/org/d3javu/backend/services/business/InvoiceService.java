package org.d3javu.backend.services.business;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.d3javu.backend.grpc.InvoiceIssueRequest;
import org.d3javu.backend.grpc.InvoiceIssueResponse;
import org.d3javu.backend.grpc.InvoiceServiceGrpc;
import org.d3javu.backend.repository.business.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService extends InvoiceServiceGrpc.InvoiceServiceImplBase {

    private final InvoiceRepository invoiceRepository;

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

    private String generateInvoicePaymentLink(UUID invoiceId) {
        return String.format("http://localhost:8080/transaction/invoice?id=%s", invoiceId.toString());
    }

}
