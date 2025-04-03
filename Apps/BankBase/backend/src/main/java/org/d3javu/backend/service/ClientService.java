package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.client.ClientCreateRecord;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.model.account.Account;
import org.d3javu.backend.model.client.Client;
import org.d3javu.backend.repository.ClientRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
//@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(@Lazy ClientRepository clientRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CompactClientReadDto> getAllClients(){
        return this.clientRepository.findAllICompactClientReadDto();
    }

    @Deprecated
    @Transactional
    public CompactClientReadDto registration(ClientCreateRecord clientCreateRecord) {
        // kafka logic instead
        var id = this.clientRepository.saveAndFlush(
                new Client(
                clientCreateRecord.surname(),
                clientCreateRecord.name(),
                clientCreateRecord.patronymic(),
                clientCreateRecord.dateOfBirth(),
                clientCreateRecord.phoneNumber(),
                null,
                clientCreateRecord.email(),
                passwordEncoder.encode(clientCreateRecord.password()),
                List.of(new Account())
        )).getId();
        return this.clientRepository.findCompactById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
        return this.clientRepository.findIdByEmail(email).orElseThrow(() -> new NoSuchElementException("No Client found with email: " + email));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.clientRepository.findByEmail(username)
                .map(en -> new User(
                        en.getEmail(),
                        en.getPasswordHash(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "test"));
    }
}
