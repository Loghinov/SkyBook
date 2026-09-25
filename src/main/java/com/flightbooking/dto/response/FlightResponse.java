package com.flightbooking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightResponse(
        Long id,
        String flightNumber,
        String origin,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        BigDecimal price,
        int totalSeats,
        int availableSeats
) {}
