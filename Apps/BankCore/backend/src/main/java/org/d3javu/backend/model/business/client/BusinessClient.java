package org.d3javu.backend.model.business.client;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="business_client")
@NoArgsConstructor
@Getter
@Setter
public class BusinessClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "official_name")
    private String officialName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;


}
