package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.model.account.Account;
import org.d3javu.backend.model.client.Client;
import org.d3javu.backend.model.transaction.TransactionType;
import org.d3javu.backend.model.transaction.Transaction;
import org.d3javu.backend.repository.ClientRepository;
import org.d3javu.backend.repository.TransactionRepository;
import org.d3javu.backend.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController implements CommandLineRunner {

    private final ClientRepository clientRepository;

    private final TransactionService transactionService;

    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        var client1 = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                LocalDate.of(2000, 1 ,12),
                "88005553535",
                null,
                "vanya@jmail.org",
                "ababa",
                List.of(new Account())
        );
        var client2 = new Client(
                "Petrov",
                "Petr",
                "Petrovich",
                LocalDate.of(1990, 10 ,1),
                "88005555555",
                null,
                "petay@jmail.org",
                "gagaga",
                List.of(new Account())
        );
        client1.getAccounts().getFirst().setAccountDeposit(500d);
        client2.getAccounts().getFirst().setAccountDeposit(500d);

        client1 = this.clientRepository.saveAndFlush(client1);
        client2 = this.clientRepository.saveAndFlush(client2);

        var transaction = new Transaction(
                client1.getAccounts().getFirst(),
                client2.getAccounts().getFirst(),
                300d,
                TransactionType.TRANSFER
        );
        this.transactionService.transfer(transaction);
        this.transactionRepository.saveAndFlush(transaction);
        var transaction1 = new Transaction(
                client1.getAccounts().getFirst(),
                client2.getAccounts().getFirst(),
                300d,
                TransactionType.TRANSFER
        );
        this.transactionService.transfer(transaction1);
        this.transactionRepository.saveAndFlush(transaction1);
    }

    @GetMapping
    public String test(){
        return "yooooo";
    }
}
