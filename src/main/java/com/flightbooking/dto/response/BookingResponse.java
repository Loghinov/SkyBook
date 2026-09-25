package com.flightbooking.dto.response;

import com.flightbooking.entity.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long userId,
        String userFullName,
        Long flightId,
        String flightNumber,
        BookingStatus status,
        int numberOfPassengers,
        BigDecimal totalPrice,
        LocalDateTime bookedAt
) {}
