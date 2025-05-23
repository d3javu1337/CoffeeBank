package org.d3javu.emailconfirmationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableMongoRepositories
@EnableKafka
@SpringBootApplication
public class EmailConfirmationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailConfirmationServiceApplication.class, args);
    }

}
