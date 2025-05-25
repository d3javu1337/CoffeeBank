package org.d3javu.backend.repository;

import org.d3javu.backend.model.account.Account;
import org.d3javu.backend.model.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {


    @Query(value = "select count(*) > 0 from account a " +
            "join client c on a.client_id = c.id " +
            "where a.id= :accountId and a.client_id= :clientId and c.email= :clientEmail",
            nativeQuery = true)
    Boolean checkOwning(Long accountId, Long clientId, String clientEmail);


    @Transactional
    @Modifying
    @Query(value = "update account set account_name= :newName where id= :accountId",
            nativeQuery = true)
    void renameAccount(Long accountId, String newName);

    @Transactional
    @Query(value = "insert into account(client_id, type) values (:clientId, :accountType) returning id",
            nativeQuery = true)
    Long createAccount(Long clientId, AccountType accountType);

    @Query(value = "select a.id from account a where a.client_id= :clientId limit 1",
            nativeQuery = true)
    Long findAccountByClientId(Long clientId);

    @Query(value = "select a.deposit >= :money from account a where a.id= :accountId",
            nativeQuery = true)
    Boolean hasEnoughMoney(Long accountId, Double money);

}
