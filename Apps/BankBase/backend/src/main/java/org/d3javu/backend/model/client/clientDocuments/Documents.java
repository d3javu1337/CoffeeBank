package org.d3javu.backend.model.client.clientDocuments;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.client.clientDocuments.identification.InternationalPassport;
import org.d3javu.backend.model.client.clientDocuments.identification.Passport;
import org.d3javu.backend.model.client.clientDocuments.insurance.compulsory.CompulsoryInsurance;
import org.d3javu.backend.model.client.clientDocuments.insurance.pension.InsuranceCertificate;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "passport_id", nullable = false)
    private Passport passport;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "international_passport_id")
    private InternationalPassport internationalPassport;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id")
    private CompulsoryInsurance compulsoryInsurance;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_certificate_id")
    private InsuranceCertificate insuranceCertificate;

    @Column(name = "itn")
    private String itn;

    public Documents(Passport passport) {
        this.passport = passport;
    }
}
