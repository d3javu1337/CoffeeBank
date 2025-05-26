package org.d3javu.backend.repository;

import org.d3javu.backend.dto.account.CompactAccountReadDto;
import org.d3javu.backend.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Transactional
    @Query(value = "insert into " +
            "account(account_name, deposit, client_id, type) " +
            "values('Счёт', 0.0, :clientId, 'PERSONAL') " +
            "returning id",
            nativeQuery = true)
    Long createAccount(Long clientId);

    @Query(value = "select a.id as id, " +
            "a.account_name as accountName, " +
            "a.deposit as accountDeposit, " +
            "a.type as accountType " +
            "from account a " +
            "where a.client_id= :clientId",
            nativeQuery = true)
    Optional<CompactAccountReadDto> findAccount(Long clientId);

    @Query(value = "select count(*) > 0 from account a where a.id= :accountId and a.client_id= :clientId",
            nativeQuery = true)
    Boolean isClientOwnsAccount(Long clientId, Long accountId);

    @Query(value = "select a.id from account a where a.client_id= :clientId",
            nativeQuery = true)
    Long findAccountIdByClientId(Long clientId);

}
