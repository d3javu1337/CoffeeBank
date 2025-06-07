package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.model.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final org.d3javu.backend.grpc.TransactionServiceGrpc.TransactionServiceBlockingStub transactionServiceStub;

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
}
