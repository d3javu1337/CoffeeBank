package org.d3javu.backend.model.client.clientDocuments.identification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.d3javu.backend.model.client.clientDocuments.assets.EGender;

import java.sql.Date;


@Entity
@Table(name = "international_passport")
@Getter
@Setter
public class InternationalPassport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number", nullable = false, length = 10, unique = true)
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

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "date_of_issue", nullable = false)
    private Date dateOfIssue;

    @Column(name = "date_of_expiration", nullable = false)
    private Date dateOfExpiration;

    public InternationalPassport() {}

    public InternationalPassport(String number, String surname, String name,
                                 String patronymic, Date dateOfBirth, String placeOfBirth,
                                 EGender gender, String department, Date dateOfIssue, Date dateOfExpiration) {
        this.number = number;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.gender = gender;
        this.department = department;
        this.dateOfIssue = dateOfIssue;
        this.dateOfExpiration = dateOfExpiration;
    }
}
