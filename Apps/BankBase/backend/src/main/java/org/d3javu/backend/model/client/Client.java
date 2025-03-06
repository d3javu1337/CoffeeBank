package org.d3javu.backend.model.client;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.account.PersonalAccount;
import org.d3javu.backend.model.client.clientDocuments.Documents;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "patronymic", nullable = false, length = 50)
    private String patronymic;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", nullable = false, length = 30)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "documents_id", nullable = false)
    private Documents documents;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "linkedClient", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PersonalAccount> personalAccounts;

    @Column(name = "blocked", nullable = false)
    private boolean isBlocked = false;
}
