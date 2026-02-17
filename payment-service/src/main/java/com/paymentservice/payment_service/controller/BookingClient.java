package com.paymentservice.payment_service.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "ticket-service",url = "http://localhost:8084")
public interface BookingClient {

    @PutMapping("/bookings/{bookingId}/confirm")
    void confirmBooking(@PathVariable("bookingId") Long bookingId);

    @PostMapping("/internal/bookings/{bookingId}/cancel")
    void cancelBooking(@PathVariable("bookingId") Long bookingId);
    
}
