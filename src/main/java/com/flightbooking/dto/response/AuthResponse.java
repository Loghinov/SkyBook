package com.flightbooking.dto.response;

public record AuthResponse(
        String token,
        String username,
        String role,
        Long userId,
        String fullName
) {}
