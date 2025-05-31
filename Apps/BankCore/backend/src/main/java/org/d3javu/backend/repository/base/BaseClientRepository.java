package org.d3javu.backend.repository.base;

import org.d3javu.backend.model.base.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(isolation = Isolation.SERIALIZABLE)
public interface BaseClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "insert into " +
            "client(surname, name, patronymic, date_of_birth, phone_number, email, password_hash, is_enabled) " +
            "values (:surname, :name, :patronymic, :dateOfBirth, :phoneNumber, :email, :passwordHash, false) " +
            "returning id",
            nativeQuery = true)
    Long registration(
            String surname, String name, String patronymic, LocalDate dateOfBirth,
            String phoneNumber, String email, String passwordHash);

    @Modifying
    @Query(value = "update client set is_enabled=true where email= :email",
            nativeQuery = true)
    void confirmEmail(String email);

    @Transactional(readOnly = true)
    @Query(value = "select c.id from client c where c.phone_number= :phoneNumber", nativeQuery = true)
    Optional<Long> findClientByPhoneNumber(String phoneNumber);

}
