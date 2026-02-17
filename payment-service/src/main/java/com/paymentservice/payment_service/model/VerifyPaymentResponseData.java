package com.paymentservice.payment_service.model;

public class VerifyPaymentResponseData {
    private Long id; 
    private String status; 
    private String reference;
    private Long amount;
    private String currency;
    private String gateway_response;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getGateway_response() {
        return gateway_response;
    }

    public void setGateway_response(String gateway_response) {
        this.gateway_response = gateway_response;
    }
}
