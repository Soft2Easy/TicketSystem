package com.ticketservice.ticket.service;

import org.springframework.stereotype.Service;

import com.ticketservice.ticket.errorHandling.ResourceNotFoundException;
import com.ticketservice.ticket.model.Ticket;
import com.ticketservice.ticket.repository.EventClient;
import com.ticketservice.ticket.repository.TicketRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;



@Service
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    public TicketService(TicketRepository ticketRepository, EventClient eventClient) {
        this.ticketRepository = ticketRepository;
        this.eventClient = eventClient;
    }

    @Transactional
    public Ticket createTicket(Ticket ticket) {

        if (ticket.getEventId() == null) {
            throw new ResourceNotFoundException("Event ID is required");
        }

        if (ticket.getAvailableQuantity() <= 0) {
            throw new IllegalArgumentException("Ticket quantity must be greater than zero");
        }   

        try {
            eventClient.checkEvent(ticket.getEventId());
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("Event with ID " + ticket.getEventId() + " does not exist");
        }

        return ticketRepository.save(ticket);
    }

    public Iterable<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Ticket with ID " + id + " not found"));
    }

    
}
