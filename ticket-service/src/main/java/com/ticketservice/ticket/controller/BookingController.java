package com.ticketservice.ticket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketservice.ticket.model.Booking;
import com.ticketservice.ticket.service.BookingService;


@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    public BookingController(BookingService bookingService) {

        this.bookingService = bookingService;
    }   

    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking savedBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.confirmBooking(bookingId);
        return ResponseEntity.ok().build();
    }
     
}
