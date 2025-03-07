package org.d3javu.backend.model.client.clientDocuments.insurance.pension;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.d3javu.backend.model.client.clientDocuments.assets.EGender;

import java.sql.Date;

@Entity
@Table(name = "insurance_certificate")
@Getter
@Setter
public class InsuranceCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "place_of_birth", nullable = false)
    private String placeOfBirth;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Column(name = "registration_date", nullable = false)
    private Date registrationDate;

    public InsuranceCertificate() {}

    public InsuranceCertificate(String number, String surname,
                                String name, String patronymic,
                                Date dateOfBirth, String placeOfBirth, EGender gender, Date registrationDate) {
        this.number = number;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.gender = gender;
        this.registrationDate = registrationDate;
    }
}
