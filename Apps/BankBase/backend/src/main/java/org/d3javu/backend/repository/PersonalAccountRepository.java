package org.d3javu.backend.repository;

import org.d3javu.backend.model.account.PersonalAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Long> {
}
