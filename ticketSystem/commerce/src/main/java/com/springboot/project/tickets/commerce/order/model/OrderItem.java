package com.springboot.project.tickets.commerce.order.model;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer eventId;

    @Column(nullable = false)
    private Integer ticketProductId;

    private Integer quantity;

    private Integer priceAtPurchase;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getTicketProductId() {
        return ticketProductId;
    }

    public void setTicketProductId(Integer ticketProductId) {
        this.ticketProductId = ticketProductId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(Integer priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

}
