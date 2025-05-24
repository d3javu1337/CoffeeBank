package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.model.transaction.Transaction;
//import org.springframework.grpc.client.GrpcClient;
//import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TransactionService {

    private final ZoneId zoneId = ZoneId.of("Europe/Moscow");

    public boolean transferByPhoneNumber(String recipientPhoneNumber, Long senderId, Double money) {
        return false;
    }

    private boolean purchase(Transaction transaction) {  return false;  }

}
