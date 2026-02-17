package com.paymentservice.payment_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentservice.payment_service.model.PaymentRequest;
import com.paymentservice.payment_service.model.PaymentResponse;
import com.paymentservice.payment_service.service.PaymentService;


@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest request) {
        System.out.println("Received payment initiation request: " + request);
        PaymentResponse payment = paymentService.initiatePayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    } 

    @GetMapping("/verify/{reference}")
    public ResponseEntity<Boolean> verifyPayment(@PathVariable("reference") String reference) {
        Boolean isVerified = paymentService.verifyPayment(reference);
        return ResponseEntity.ok(isVerified);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
        @RequestBody String payload,
        @RequestHeader("x-paystack-signature") String signature) {
        System.out.println("RECIEVING WEBHOOK OF ID: " + payload);
        paymentService.processWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Payment Service is running");
    }

    
    
}
