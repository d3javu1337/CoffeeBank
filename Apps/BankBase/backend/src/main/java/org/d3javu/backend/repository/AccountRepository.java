package org.d3javu.backend.repository;

import org.d3javu.backend.dto.account.CompactAccountReadDto;
import org.d3javu.backend.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "select a.account_name, a.deposit, a.type from account a where a.client_id= :id",
            nativeQuery = true)
    List<CompactAccountReadDto> findAllAccountsByLinkedClientId(Long id);

}
