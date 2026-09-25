package com.flightbooking.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProcessPaymentRequest(
        @NotNull Long bookingId
) {}
