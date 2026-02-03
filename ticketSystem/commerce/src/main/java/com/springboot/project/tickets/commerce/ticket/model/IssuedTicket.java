package com.springboot.project.tickets.commerce.ticket.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class IssuedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer orderItemId;

    private Integer eventId;

    private Integer ticketProductId;

    private String qrCode;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;


    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getTicketProductId() {
        return ticketProductId;
    }

    public void setTicketProductId(Integer ticketProductId) {
        this.ticketProductId = ticketProductId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public enum TicketStatus {
    ACTIVE,
    USED,
    CANCELLED
}
}
