package org.d3javu.backend.repository.base;

import org.d3javu.backend.model.base.personalaccount.PersonalAccount;
import org.d3javu.backend.model.base.personalaccount.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Long> {


    @Query(value = "select count(*) > 0 from personal_account a " +
            "join client c on a.client_id = c.id " +
            "where a.id= :accountId and a.client_id= :clientId and c.email= :clientEmail",
            nativeQuery = true)
    Boolean checkOwning(Long accountId, Long clientId, String clientEmail);


    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query(value = "update personal_account set name= :newName where id= :accountId",
            nativeQuery = true)
    void renameAccount(Long accountId, String newName);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Query(value = "insert into personal_account(client_id, type, name) values (:clientId, :accountType, :name) returning id",
            nativeQuery = true)
    Long createAccount(Long clientId, AccountType accountType, String name);

    @Query(value = "select a.id from personal_account a where a.client_id= :clientId limit 1",
            nativeQuery = true)
    Long findAccountByClientId(Long clientId);

    @Query(value = "select a.deposit >= :money from personal_account a where a.id= :accountId",
            nativeQuery = true)
    Boolean hasEnoughMoney(Long accountId, Double money);

}
