package org.d3javu.backend.repository;

import org.d3javu.backend.model.client.clientDocuments.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Documents, Long> {
}
