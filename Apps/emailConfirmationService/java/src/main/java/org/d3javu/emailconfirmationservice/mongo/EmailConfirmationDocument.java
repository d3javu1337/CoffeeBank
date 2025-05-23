package org.d3javu.emailconfirmationservice.mongo;

import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "EmailConfirmation")
public class EmailConfirmationDocument {

    @Id
    private String email;

    private String token;

    public EmailConfirmationDocument(String email) {
        this.email = email;
        this.token = Hashing.sha256().hashBytes(email.getBytes()).toString();
    }
}
