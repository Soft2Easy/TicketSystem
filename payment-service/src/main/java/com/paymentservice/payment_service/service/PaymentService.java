package com.paymentservice.payment_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.paymentservice.payment_service.controller.BookingClient;
import com.paymentservice.payment_service.model.Payment;
import com.paymentservice.payment_service.model.PaymentRequest;
import com.paymentservice.payment_service.model.PaymentResponse;
import com.paymentservice.payment_service.model.PaymentVerification;
import com.paymentservice.payment_service.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingClient bookingClient;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${paystack.secret.key}")
    private String paystackSecret;

    public PaymentService(PaymentRepository paymentRepository, BookingClient bookingClient, RestTemplate restTemplate) {
        this.paymentRepository = paymentRepository;
        this.bookingClient = bookingClient;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        //Validate input
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be provided and greater than 0");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email must be provided");
        }

        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setAmount(request.getAmount());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setProvider("StackPay");
        payment.setCurrency("ZAR");
        payment.setGatewayResponse("Payment record created, awaiting initialization");
        payment = paymentRepository.save(payment);

        //Call Paystack
        PaymentResponse response;
        try {
            response = initiateTransaction(request);
        } catch (Exception e) {
            // rollback DB transaction automatically
            throw new RuntimeException("Failed to initialize Paystack transaction", e);
        }

        // Update payment to INITIATED
        payment.setProviderRef(response.getData().getReference());
        payment.setStatus(Payment.PaymentStatus.INITIATED);
        paymentRepository.save(payment);

        return response;
    }

    private PaymentResponse initiateTransaction(PaymentRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paystackSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        BigDecimal amountInCents = request.getAmount()
                .multiply(BigDecimal.valueOf(100));

        Map<String, Object> body = new HashMap<>();
        body.put("amount", amountInCents.longValue());
        body.put("email", request.getEmail());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                "https://api.paystack.co/transaction/initialize",
                HttpMethod.POST,
                entity,
                PaymentResponse.class
        );

        return response.getBody();
    }

    @Transactional
    public boolean verifyPayment(String reference) {

        Payment payment = paymentRepository
                .findByProviderRef(reference)
                .orElseThrow(() -> new IllegalStateException("Payment not found"));

        if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
            return true;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paystackSecret); // inject via @Value("${paystack.secret.key}")
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PaymentVerification> response = restTemplate.exchange(
                "https://api.paystack.co/transaction/verify/" + reference,
                HttpMethod.GET,
                entity,
                PaymentVerification.class
        );

        PaymentVerification verification = response.getBody();

        if (verification == null || !verification.isStatus()) {
            markFailed(payment, "Verification failed");
            return false;
        }

        boolean success = completePayment(payment, verification);

        return success;
    }

    private boolean completePayment(Payment payment, PaymentVerification verification) {
        var data = verification.getData();

        if (!"success".equalsIgnoreCase(data.getStatus())) {
            markFailed(payment, data.getGateway_response());
            return false;
        }

        BigDecimal expectedAmountInCents = payment.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP);
        BigDecimal actualAmountInCents = BigDecimal.valueOf(data.getAmount());

        if (expectedAmountInCents.compareTo(actualAmountInCents) != 0) {
            markFailed(payment, "Amount mismatch");
            return false;
        }

        if (!payment.getCurrency().equalsIgnoreCase(data.getCurrency())) {
            markFailed(payment, "Currency mismatch");
            return false;
        }

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setGatewayResponse(data.getGateway_response());
        payment.setProviderTransactionId(String.valueOf(data.getId()));

        paymentRepository.save(payment);

        bookingClient.confirmBooking(payment.getBookingId());

        return true;
    }

    private void markFailed(Payment payment, String gatewayResponse) {
        payment.setStatus(Payment.PaymentStatus.FAILED);
        payment.setGatewayResponse(gatewayResponse);
        paymentRepository.save(payment);
    }

    @Transactional
    public void processWebhook(String payload, String signature) {

        // 1️ Verify signature first
        if (!isValidSignature(payload, signature)) {
            throw new IllegalArgumentException("Invalid webhook signature");
        }

        try {
            JsonNode root = objectMapper.readTree(payload);

            String event = root.get("event").asText();

            if (!"charge.success".equals(event)) {
                return; // ignore other events
            }

            JsonNode data = root.get("data");
            String reference = data.get("reference").asText();

            Payment payment = paymentRepository
                    .findByProviderRef(reference)
                    .orElseThrow(() -> new IllegalStateException("Payment not found"));

            // Idempotency check
            if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
                return;
            }

            // Reuse your verification logic
            verifyPayment(reference);

        } catch (Exception e) {
            throw new RuntimeException("Webhook processing failed", e);
        }
    }

    private boolean isValidSignature(String payload, String signature) {
        try {
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(
                    paystackSecret.getBytes(),
                    "HmacSHA512"
            );

            sha512Hmac.init(secretKey);
            byte[] hash = sha512Hmac.doFinal(payload.getBytes());

            String computedSignature = HexFormat.of().formatHex(hash);

            return computedSignature.equalsIgnoreCase(signature);

        } catch (Exception e) {
            throw new RuntimeException("Signature verification failed", e);
        }
    }

}
