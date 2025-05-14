package org.d3javu.backend.model.client.clientDocuments.insurance.compulsory;//package org.d3javu.backend.model.client.clientDocuments.insurance.compulsory;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import org.d3javu.backend.model.client.clientDocuments.assets.EGender;
//
//import java.sql.Date;
//
//@Entity
//@Table(name = "compulsory_insurance", uniqueConstraints = @UniqueConstraint(columnNames = {"number", "serial_number"}))
//@Getter
//@Setter
//public class CompulsoryInsurance {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "number", nullable = false)
//    private String number;
//
//    @Column(name = "serial_number", nullable = false)
//    private String serialNumber;
//
//    @Column(name = "surname", nullable = false)
//    private String surname;
//
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @Column(name = "patronymic", nullable = false)
//    private String patronymic;
//
//    @Column(name = "date_of_birth", nullable = false)
//    private Date dateOfBirth;
//
//    @Column(name = "gender", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private EGender gender;
//
//    @Column(name = "date_of_issue", nullable = false)
//    private Date dateOfIssue;
//
//    public CompulsoryInsurance() {}
//
//    public CompulsoryInsurance(String number, String serialNumber, String surname,
//                               String name, String patronymic, Date dateOfBirth, EGender gender, Date dateOfIssue) {
//        this.number = number;
//        this.serialNumber = serialNumber;
//        this.surname = surname;
//        this.name = name;
//        this.patronymic = patronymic;
//        this.dateOfBirth = dateOfBirth;
//        this.gender = gender;
//        this.dateOfIssue = dateOfIssue;
//    }
//}
