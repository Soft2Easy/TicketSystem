package com.paymentservice.payment_service.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
    @NotNull
    private Long bookingId;

    @NotNull
    private BigDecimal amount;
 
    @NotNull
    private String email;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
