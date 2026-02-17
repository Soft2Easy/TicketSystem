package com.paymentservice.payment_service.model;


public class PaymentResponse {
    private boolean status;
    private String message;
    private InitializePaymentResponseData data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InitializePaymentResponseData getData() {
        return data;
    }

    public void setData(InitializePaymentResponseData data) {
        this.data = data;
    }
}
