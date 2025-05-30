package org.d3javu.backend.model.base.client.clientDocuments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.base.client.Client;
import org.d3javu.backend.model.base.client.clientDocuments.identification.Passport;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
//    @Column(name = "client_id")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "passport_id", nullable = false)
    private Passport passport;

    @Column(name = "itn", length = 12, nullable = false, updatable = false, unique = true)
    private String itn;

    public Documents(Passport passport) {
        this.passport = passport;
    }
}
