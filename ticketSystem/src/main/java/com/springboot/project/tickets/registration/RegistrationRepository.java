package com.springboot.project.tickets.registration;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface RegistrationRepository extends MongoRepository<Registration, String> {
    
   Optional<Registration> findByTicketCode(String ticketCode);

    void deleteByTicketCode(String ticketCode);

}
