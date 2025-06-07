package org.d3javu.backend.services;

import grpcstarter.server.GrpcService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.d3javu.backend.grpc.*;
import org.d3javu.backend.model.transaction.TransactionType;
import org.d3javu.backend.repository.TransactionRepository;
import org.d3javu.backend.repository.business.PaymentRepository;
import org.d3javu.backend.services.base.PersonalAccountService;
import org.d3javu.backend.services.base.BaseClientService;
import org.d3javu.backend.services.business.InvoiceService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@GrpcService
public class TransactionService extends TransactionServiceGrpc.TransactionServiceImplBase {

    private final TransactionRepository transactionRepository;
    private final BaseClientService baseClientService;
    private final PersonalAccountService personalAccountService;
    private final InvoiceService invoiceService;
    private final PaymentRepository paymentRepository;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void transferByPhoneNumber(TransferByPhoneNumberRequest request,
                                      StreamObserver<TransferByPhoneNumberResponse> responseObserver) {
        var recipientAccountId = this.personalAccountService.getAccountIdByClientId(this.baseClientService.getClientIdByPhoneNumber(request.getRecipientPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("No client with phone number " + request.getRecipientPhoneNumber())));
        var senderAccountId = request.getSenderAccountId();
        if (!this.personalAccountService.hasEnoughMoney(senderAccountId, request.getAmount())) {
            this.transactionRepository.createTransaction(senderAccountId, recipientAccountId, request.getAmount(),
                    TransactionType.TRANSFER.name(), false);
            responseObserver.onNext(TransferByPhoneNumberResponse.newBuilder().setIsCompleted(false).build());
            responseObserver.onCompleted();
            return;
        }
        this.transactionRepository.takeMoneyFromSender(senderAccountId, request.getAmount());
        this.transactionRepository.sendMoneyToRecipient(recipientAccountId, request.getAmount());
        this.transactionRepository.createTransaction(senderAccountId, recipientAccountId, request.getAmount(),
                TransactionType.TRANSFER.name(), true);
        responseObserver.onNext(TransferByPhoneNumberResponse.newBuilder().setIsCompleted(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void invoicePayment(InvoicePaymentRequest request, StreamObserver<InvoicePaymentResponse> responseObserver) {
        if (!this.invoiceService.existsInvoice(request.getInvoiceUUID())){
            responseObserver.onNext(InvoicePaymentResponse.newBuilder().setIsCompleted(false).build());
            responseObserver.onCompleted();
            return;
        }
        var invoiceAmount = this.invoiceService.getInvoiceAmount(request.getInvoiceUUID());
        if(!this.personalAccountService.hasEnoughMoney(request.getPayerAccountId(), invoiceAmount)){
            this.transactionRepository.createTransaction(request.getPayerAccountId(), null, invoiceAmount,
                    TransactionType.PURCHASE.name(), false);
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
                null, invoiceAmount, TransactionType.PURCHASE.name(), true);
        this.paymentRepository.createPayment(providerAccountId, request.getPayerAccountId(),
                transactionId, UUID.fromString(request.getInvoiceUUID()));
        responseObserver.onNext(InvoicePaymentResponse.newBuilder().setIsCompleted(true).build());
        responseObserver.onCompleted();
    }


}
