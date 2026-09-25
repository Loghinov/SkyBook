package com.flightbooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateBookingRequest(
        @NotNull Long userId,
        @NotNull Long flightId,
        @Min(1) int numberOfPassengers
) {}
