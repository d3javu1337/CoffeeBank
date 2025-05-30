package org.d3javu.backend.repository.business;

import org.d3javu.backend.model.business.client.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
}
