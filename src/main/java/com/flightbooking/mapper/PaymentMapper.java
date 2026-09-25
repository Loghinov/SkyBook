package com.flightbooking.mapper;

import com.flightbooking.dto.response.PaymentResponse;
import com.flightbooking.entity.Payment;

public class PaymentMapper {
    private PaymentMapper() {}

    public static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getBooking().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getProcessedAt()
        );
    }
}
