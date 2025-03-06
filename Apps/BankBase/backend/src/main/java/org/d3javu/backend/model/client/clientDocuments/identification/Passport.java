package org.d3javu.backend.model.client.clientDocuments.identification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.d3javu.backend.model.client.clientDocuments.assets.EGender;

import java.sql.Date;


@Entity
@Table(name = "passport")
@Getter
@Setter
public class Passport {

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

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "code_of_department", nullable = false)
    private String codeOfDepartment;

    @Column(name = "date_of_issue", nullable = false)
    private Date dateOfIssue;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Column(name = "apartment_number", nullable = false)
    private int apartmentNumber;

    public Passport() {}

    public Passport(String number, String surname, String name, String patronymic, Date dateOfBirth,
                    EGender gender, String department, String codeOfDepartment, Date dateOfIssue,
                    String region, String city, String street, String houseNumber, int apartmentNumber) {
        this.number = number;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.department = department;
        this.codeOfDepartment = codeOfDepartment;
        this.dateOfIssue = dateOfIssue;
        this.region = region;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }
}
