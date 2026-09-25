package com.flightbooking.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String role,
        LocalDateTime createdAt
) {}
