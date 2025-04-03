package org.d3javu.backend.repository;

import org.d3javu.backend.dto.client.ClientAuthData;
import org.d3javu.backend.dto.client.ClientCreateRecord;
import org.d3javu.backend.model.client.Client;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "select c.id, c.surname, c.name, c.patronymic from client c",
            nativeQuery = true)
    List<CompactClientReadDto> findAllICompactClientReadDto();

    @Query(value = "select c.id, c.surname, c.name, c.patronymic from client c where c.id= :clientId",
            nativeQuery = true)
    Optional<CompactClientReadDto> findCompactById(Long clientId);

    @Query(value = "select c.email, c.password_hash from client c where email= :email",
            nativeQuery = true)
    Optional<ClientAuthData> findByEmail(String email);

    @Query(value = "select c.id, c.surname, c.name, c.patronymic from client c where c.email= :email",
            nativeQuery = true)
    Optional<CompactClientReadDto> findCompactByEmail(String email);

    @Query(value = "select c.id from client c where c.email= :email",
            nativeQuery = true)
    Optional<Long> findIdByEmail(String email);

//    @Transactional
//    @Query(value = "insert into client (surname, name, patronymic, date_of_birth, email, phone_number, password_hash) " +
//            "values (:en.surname, :en.name, :en.patronymic, :en.dateOfBirth, en.email, en.phone_number, )",
//            nativeQuery = true)
//    Long createClient(ClientCreateRecord en);

}
