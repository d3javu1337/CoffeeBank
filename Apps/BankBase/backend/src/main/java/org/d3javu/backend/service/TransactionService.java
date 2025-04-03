package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.model.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TransactionService {

    private final ZoneId zoneId = ZoneId.of("Europe/Moscow");

    public boolean transfer(Transaction transaction) {

        transaction.setCommitedAt(LocalDateTime.now(this.zoneId));
        transaction.setFromName(transaction.getFrom().getLinkedClient().getName());
        transaction.setToName(transaction.getTo().getLinkedClient().getName());

        if(transaction.getFrom().getAccountDeposit() < transaction.getMoney()){
            transaction.setCompleted(false);
            //SEND TO KAFKA HERE
            log.info("transaction rollbacked");
            return false;
        }

        transaction.getFrom().setAccountDeposit(transaction.getFrom().getAccountDeposit() - transaction.getMoney());
        transaction.getTo().setAccountDeposit(transaction.getTo().getAccountDeposit() + transaction.getMoney());

        //SEND TO KAFKA HERE

        log.info("transaction committed");
        transaction.setCompleted(true);

        return true;
    }

    private boolean purchase(Transaction transaction) {  return false;  }

}
