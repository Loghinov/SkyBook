package com.flightbooking.dto.response;

import com.flightbooking.entity.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long bookingId,
        BigDecimal amount,
        PaymentStatus status,
        String transactionId,
        LocalDateTime processedAt
) {}
