package org.d3javu.backend.dto.client;

import java.time.LocalDate;

public interface ClientCreateDto {

    String getSurname();
    String getName();
    String getPatronymic();
    LocalDate getDateOfBirth();
    String getPhoneNumber();
    String getEmail();

}
