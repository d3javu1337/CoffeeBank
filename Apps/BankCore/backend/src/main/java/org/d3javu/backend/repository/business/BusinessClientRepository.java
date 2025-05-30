package org.d3javu.backend.repository.business;

import org.d3javu.backend.model.business.client.BusinessClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessClientRepository extends JpaRepository<BusinessClient, Long> {
}
