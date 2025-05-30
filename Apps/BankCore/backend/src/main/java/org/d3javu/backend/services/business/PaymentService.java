package org.d3javu.backend.services.business;

import grpcstarter.server.GrpcService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.d3javu.backend.grpc.InvoicePaymentRequest;
import org.d3javu.backend.grpc.InvoicePaymentResponse;
import org.d3javu.backend.grpc.PaymentServiceGrpc;
import org.d3javu.backend.model.transaction.TransactionType;
import org.d3javu.backend.repository.TransactionRepository;
import org.d3javu.backend.repository.business.PaymentRepository;
import org.d3javu.backend.services.base.AccountService;

@RequiredArgsConstructor
@GrpcService
public class PaymentService extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final InvoiceService invoiceService;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void invoicePayment(InvoicePaymentRequest request, StreamObserver<InvoicePaymentResponse> responseObserver) {
        if (!this.invoiceService.existsInvoice(request.getInvoiceUUID())){
            responseObserver.onNext(InvoicePaymentResponse.newBuilder().setIsCompleted(false).build());
            responseObserver.onCompleted();
            return;
        }
        var invoiceAmount = this.invoiceService.getInvoiceAmount(request.getInvoiceUUID());
        if(!this.accountService.hasEnoughMoney(request.getPayerAccountId(), invoiceAmount)){
            this.transactionRepository.createTransaction(request.getPayerAccountId(), null, invoiceAmount,
                    TransactionType.PURCHASE, false);
            responseObserver.onNext(InvoicePaymentResponse.newBuilder().setIsCompleted(false).build());
            responseObserver.onCompleted();
            return;
        }
        var providerAccountId = this.invoiceService.getRecipientPaymentAccountId(request.getInvoiceUUID());
        this.paymentRepository.takeMoneyFromPayer(request.getPayerAccountId(), invoiceAmount);
        this.paymentRepository.sendMoneyToRecipient(
                providerAccountId,
                invoiceAmount);
        var transactionId = this.transactionRepository.createTransaction(request.getPayerAccountId(),
                null, invoiceAmount, TransactionType.PURCHASE, true);
        this.paymentRepository.createPayment(providerAccountId, request.getPayerAccountId(), transactionId);
        responseObserver.onNext(InvoicePaymentResponse.newBuilder().setIsCompleted(true).build());
        responseObserver.onCompleted();
    }

}
