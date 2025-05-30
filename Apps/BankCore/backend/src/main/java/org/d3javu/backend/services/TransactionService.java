package org.d3javu.backend.services;

import grpcstarter.server.GrpcService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.d3javu.backend.grpc.*;
import org.d3javu.backend.model.transaction.TransactionType;
import org.d3javu.backend.repository.TransactionRepository;
import org.d3javu.backend.services.base.AccountService;
import org.d3javu.backend.services.base.BaseClientService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@GrpcService
public class TransactionService extends TransactionServiceGrpc.TransactionServiceImplBase {

    private final TransactionRepository transactionRepository;
    private final BaseClientService baseClientService;
    private final AccountService accountService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void transferByPhoneNumber(TransferByPhoneNumberRequest request,
                                      StreamObserver<TransferByPhoneNumberResponse> responseObserver) {
        var recipientAccountId = this.accountService.getAccountIdByClientId(this.baseClientService.getClientIdByPhoneNumber(request.getRecipientPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("No client with phone number " + request.getRecipientPhoneNumber())));
        var senderAccountId = request.getSenderAccountId();
        if (!this.accountService.hasEnoughMoney(senderAccountId, request.getMoney())) {
            this.transactionRepository.createTransaction(senderAccountId, recipientAccountId, request.getMoney(),
                    TransactionType.TRANSFER, false);
            responseObserver.onNext(TransferByPhoneNumberResponse.newBuilder().setIsCompleted(false).build());
            responseObserver.onCompleted();
            return;
        }
        this.transactionRepository.takeMoneyFromSender(senderAccountId, request.getMoney());
        this.transactionRepository.sendMoneyToRecipient(recipientAccountId, request.getMoney());
        this.transactionRepository.createTransaction(senderAccountId, recipientAccountId, request.getMoney(),
                TransactionType.TRANSFER, true);
        responseObserver.onNext(TransferByPhoneNumberResponse.newBuilder().setIsCompleted(true).build());
        responseObserver.onCompleted();
    }

}
