package org.d3javu.backend.security.clientsessions;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.List;

//public interface ClientSessionsRepository extends ReactiveMongoRepository<ClientSessions, String> {
public interface ClientSessionsRepository extends MongoRepository<ClientSessions, String> {


    List<ClientSessions> findAllByEmail(String email);

    boolean existsClientSessionsByEmail(String email);

//    List<ClientSessions> findClientSessionsByEmail(String email);

    ClientSessions findClientSessionsByEmail(String email);
}
