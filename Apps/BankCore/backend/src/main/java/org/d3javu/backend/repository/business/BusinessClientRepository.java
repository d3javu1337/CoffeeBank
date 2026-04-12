package org.d3javu.backend.repository.business;

import org.d3javu.backend.model.business.client.BusinessClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(isolation = Isolation.SERIALIZABLE)
public interface BusinessClientRepository extends JpaRepository<BusinessClient, Long> {

    @Query(value = "insert into business_client(id, official_name, brand, email) " +
            "values (:id, :officialName, :brand, :email) returning id",
            nativeQuery = true)
    Long registration(String id, String officialName, String brand, String email);

    @Query(value = "select count(*)>0 from business_client bc where bc.id= :id", nativeQuery = true)
    Boolean isClientExist(String id);

}
