package com.flightbooking.controller;

import com.flightbooking.dto.request.ProcessPaymentRequest;
import com.flightbooking.dto.response.PaymentResponse;
import com.flightbooking.exception.UnauthorizedAccessException;
import com.flightbooking.security.SecurityUtils;
import com.flightbooking.service.BookingService;
import com.flightbooking.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @Valid @RequestBody ProcessPaymentRequest request, Authentication auth) {
        if (!securityUtils.isAdmin(auth)) {
            Long currentUserId = securityUtils.getCurrentUser(auth).getId();
            if (!currentUserId.equals(bookingService.getBookingById(request.bookingId()).userId())) {
                throw new UnauthorizedAccessException("You can only pay for your own bookings");
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(request));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBookingId(
            @PathVariable Long bookingId, Authentication auth) {
        if (!securityUtils.isAdmin(auth)) {
            Long currentUserId = securityUtils.getCurrentUser(auth).getId();
            if (!currentUserId.equals(bookingService.getBookingById(bookingId).userId())) {
                throw new UnauthorizedAccessException("You can only view your own payments");
            }
        }
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }
}
