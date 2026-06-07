package org.d3javu.backend.repository.business;

import org.d3javu.backend.model.business.client.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    @Query(value = "insert into contact_person(surname, name, patronymic, phone_number, email, business_client_id) " +
            "values(:surname, :name, :patronymic, :phoneNumber, :email, :businessClientId) returning id", nativeQuery = true)
    Long create(Long businessClientId, String surname, String name, String patronymic, String phoneNumber, String email);


    @Transactional
    @Query(value = "update contact_person set " +
            "surname= :surname, name= :name, patronymic= :patronymic, phone_number= :phoneNumber, email= :email " +
            "where id= :id", nativeQuery = true)
    @Modifying
    void update(Long businessClientId, Long id, String surname, String name, String patronymic, String phoneNumber, String email);

    @Transactional
    @Query(value = "delete from contact_person where id=:personId", nativeQuery = true)
    @Modifying
    void delete(Long businessClientId, Long personId);
}
