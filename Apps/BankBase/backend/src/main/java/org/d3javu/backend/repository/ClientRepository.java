package org.d3javu.backend.repository;

import org.d3javu.backend.model.client.Client;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "select c.id, c.surname, c.name, c.patronymic from client c",
            nativeQuery = true)
    List<CompactClientReadDto> findAllICompactClientReadDto();

}
