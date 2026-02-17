package com.ticketservice.ticket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketservice.ticket.model.Ticket;
import com.ticketservice.ticket.service.TicketService;


@RestController
@RequestMapping("/tickets")
public class TicketController {
    
    private final TicketService ticketService;
    
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        
         Ticket savedTicket = ticketService.createTicket(ticket);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
    }
    

    @GetMapping("/all")
    public ResponseEntity<Iterable<Ticket>> getAllTickets() {
        Iterable<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {

        Ticket ticket = ticketService.getTicketById(id);

        return ResponseEntity.ok(ticket);
    }
}
