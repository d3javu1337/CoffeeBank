package org.d3javu.backend.security.clientsessions;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.List;
import java.util.UUID;

public interface ClientSessionsRepository extends MongoRepository<ClientSessions, String> {

    List<ClientSessions> findAllByEmail(String email);

    boolean existsClientSessionsByEmail(String email);

    ClientSessions findClientSessionsByEmail(String email);
}
