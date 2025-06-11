package org.d3javu.backend.repository.business;

import org.d3javu.backend.model.business.client.BusinessClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(isolation = Isolation.SERIALIZABLE)
public interface BusinessClientRepository extends JpaRepository<BusinessClient, Long> {

    @Query(value = "insert into business_client(official_name, brand, email, password_hash) " +
            "values (:officialName, :brand, :email, :passwordHash) returning id",
            nativeQuery = true)
    Long registration(String officialName, String brand, String email, String passwordHash);

}
