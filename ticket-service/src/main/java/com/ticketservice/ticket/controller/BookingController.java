package com.ticketservice.ticket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ticketservice.ticket.model.Booking;
import com.ticketservice.ticket.model.Ticket;
import com.ticketservice.ticket.repository.BookingRepository;
import com.ticketservice.ticket.repository.TicketRepository;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    
    public BookingController(BookingRepository bookingRepository, TicketRepository ticketRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
    }   

    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {

        if (ticketRepository.findById(booking.getTicketId()).isEmpty() || booking.getQuantity() <= 0
         || booking.getQuantity() > ticketRepository.findById(booking.getTicketId()).get().getAvailableQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Booking savedBooking = bookingRepository.save(booking);
        updateTicketQuantity(booking.getTicketId(), booking.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

     private void updateTicketQuantity(Long ticketId, Integer quantity) {
        
        Ticket ticket = ticketRepository.getById(ticketId);

        int updateTicketQuantity = ticket.getAvailableQuantity() - quantity;
        ticket.setAvailableQuantity(updateTicketQuantity);
        ticketRepository.save(ticket);
     }
}
