package com.ticketservice.ticket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ticketservice.ticket.model.Ticket;
import com.ticketservice.ticket.repository.EventClient;
import com.ticketservice.ticket.repository.TicketRepository;

import feign.FeignException;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    
    private final TicketRepository ticketRepository;
    private final EventClient eventClient;
    
    public TicketController(TicketRepository ticketRepository, EventClient eventClient) {
        
        this.ticketRepository = ticketRepository;
        this.eventClient = eventClient;
    }

    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        
         try {
            eventClient.checkEvent(ticket.getEventId());
            
        } catch (FeignException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Ticket savedTicket = ticketRepository.save(ticket);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTicket);
    }
    

    @GetMapping("/all")
    public ResponseEntity<Iterable<Ticket>> getAllTickets() {
        Iterable<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    
    
}
