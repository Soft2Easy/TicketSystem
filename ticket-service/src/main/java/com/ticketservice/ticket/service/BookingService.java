package com.ticketservice.ticket.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ticketservice.ticket.errorHandling.ResourceNotFoundException;
import com.ticketservice.ticket.model.Booking;
import com.ticketservice.ticket.model.Ticket;
import com.ticketservice.ticket.repository.BookingRepository;
import com.ticketservice.ticket.repository.TicketRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingService {

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    
    public BookingService(TicketRepository ticketRepository, BookingRepository bookingRepository) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(booking.getTicketId());
        if (ticketOpt.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with ID " + booking.getTicketId() + " not found.");
        }

        if(booking.getQuantity() <= 0) {
            throw new IllegalArgumentException("Booking quantity must be greater than zero.");
        }

        if (ticketOpt.get().getAvailableQuantity() < booking.getQuantity()) {
            throw new IllegalStateException("Booking quantity exceeds available tickets.");
        }

        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.PENDING);

        ticketOpt.get().setAvailableQuantity(ticketOpt.get().getAvailableQuantity() - booking.getQuantity());
        ticketRepository.save(ticketOpt.get());


        return bookingRepository.save(booking);
    }

    @Transactional
    public void confirmBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Booking with ID " + bookingId + " not found.");
        }
        Booking booking = bookingOpt.get();
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be confirmed.");
        }
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

}
