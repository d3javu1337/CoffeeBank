//package org.d3javu.backend.model.cheque;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.d3javu.backend.model.transaction.Transaction;
//
//@Entity
//@Table(name = "cheque")
//@Getter
//@Setter
//@NoArgsConstructor
//public class Cheque {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "transaction_id")
//    private Transaction transaction;
//
//    public Cheque(Transaction transaction){
//        this.transaction = transaction;
//    }
//}
