package org.d3javu.backend.repository;

import org.d3javu.backend.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface BaseClientRepository extends JpaRepository<Client, Long> {

    @Transactional
    @Query(value = "insert into " +
            "client(surname, name, patronymic, date_of_birth, phone_number, email, password_hash, is_enabled) " +
            "values (:surname, :name, :patronymic, :dateOfBirth, :phoneNumber, :email, :passwordHash, false) " +
            "returning id",
            nativeQuery = true)
    Long registration(
            String surname, String name, String patronymic, LocalDate dateOfBirth,
            String phoneNumber, String email, String passwordHash);

    @Modifying
    @Transactional
    @Query(value = "update client set is_enabled=true where email= :email",
            nativeQuery = true)
    void confirmEmail(String email);

}
