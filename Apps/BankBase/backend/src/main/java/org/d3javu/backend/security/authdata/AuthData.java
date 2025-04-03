//package org.d3javu.backend.security.authdata;
//
//import jakarta.persistence.*;
//import org.d3javu.backend.model.client.Client;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "auth_data")
//public class AuthData {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "email",unique = true, nullable = false)
//    private String email;
//
//    @Column(name = "passord_hash", nullable = false)
//    private String passwordHash;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//}