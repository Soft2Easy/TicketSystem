package com.paymentservice.payment_service.model;

public class PaymentVerification {
    private boolean status;
    private String message;
    private VerifyPaymentResponseData data;

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

    public VerifyPaymentResponseData getData() {
        return data;
    }

    public void setData(VerifyPaymentResponseData data) {
        this.data = data;
    }
}
