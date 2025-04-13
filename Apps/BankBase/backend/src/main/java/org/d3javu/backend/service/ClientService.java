package org.d3javu.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.client.ClientCreateRecord;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.repository.ClientRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ClientService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, ClientCreateRecord> kafkaTemplate;

    public ClientService(@Lazy ClientRepository clientRepository,
                         @Lazy PasswordEncoder passwordEncoder,
                         @Lazy KafkaTemplate<String, ClientCreateRecord> kafkaTemplate) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }


    public boolean registration(ClientCreateRecord clientCreateRecord) {
        if(this.clientRepository.existsClientByEmail(clientCreateRecord.email())) return false;
        CompletableFuture.runAsync(() -> this.kafkaTemplate.send("client-registration-topic", clientCreateRecord));
        return true;
    }

    public CompactClientReadDto getClientById(Long clientId){
        return this.clientRepository.findCompactById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public CompactClientReadDto getClientByEmail(String email){
        return this.clientRepository.findCompactByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long getClientIdByEmail(String email){
        return this.clientRepository.findIdByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No Client found with email: " + email));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.clientRepository.findByEmail(username)
                .map(en -> new User(
                        en.getEmail(),
                        en.getPasswordHash(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )).orElseThrow(() -> {
                    log.warn("User not found with email: {}", username);
                    return new NotFoundException("No Client found with email: " + username);
                });
    }
}
