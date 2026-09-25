package com.flightbooking.service;

import com.flightbooking.dto.request.ProcessPaymentRequest;
import com.flightbooking.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(ProcessPaymentRequest request);
    PaymentResponse getPaymentByBookingId(Long bookingId);
    PaymentResponse refundPayment(Long paymentId);
}
