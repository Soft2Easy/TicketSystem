package com.springboot.project.tickets.registration;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "/registrations")
public class RegistrationController {
    
    private final RegistrationRepository registrationRepository;

    public RegistrationController(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @PostMapping
    public Registration create(@RequestBody Registration registration) {
        String ticketCode = UUID.randomUUID().toString();
        return registrationRepository.save(new Registration(null, registration.productId(), ticketCode, registration.attendeeName()));
    }

    @GetMapping(path = "/{ticketCode}")
    public Registration get(@PathVariable("ticketCode") String ticketCode) {
        return registrationRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new NoSuchElementException("Registration with ticket code " + ticketCode + " not found"));
    }

    @PutMapping
    public Registration update(@RequestBody Registration registration) {
        String ticketCode = registration.ticketCode();
        var existing = registrationRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new NoSuchElementException("Registration with ticket code " + ticketCode + " not found"));

        return registrationRepository.save(new Registration(
                existing.Id(),
                existing.productId(),
                ticketCode,
                registration.attendeeName()
        ));
    }

    @DeleteMapping(path = "/{ticketCode}")
    public void delete(@PathVariable("ticketCode") String ticketCode) {
        registrationRepository.deleteByTicketCode(ticketCode);
    }

}
