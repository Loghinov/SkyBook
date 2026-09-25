package com.flightbooking.controller;

import com.flightbooking.dto.request.CreateBookingRequest;
import com.flightbooking.dto.response.BookingResponse;
import com.flightbooking.exception.UnauthorizedAccessException;
import com.flightbooking.security.SecurityUtils;
import com.flightbooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request, Authentication auth) {
        if (!securityUtils.isAdmin(auth)) {
            Long currentUserId = securityUtils.getCurrentUser(auth).getId();
            if (!currentUserId.equals(request.userId())) {
                throw new UnauthorizedAccessException("You can only create bookings for yourself");
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id, Authentication auth) {
        BookingResponse booking = bookingService.getBookingById(id);
        if (!securityUtils.isAdmin(auth)) {
            Long currentUserId = securityUtils.getCurrentUser(auth).getId();
            if (!currentUserId.equals(booking.userId())) {
                throw new UnauthorizedAccessException("You can only view your own bookings");
            }
        }
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByUser(
            @PathVariable Long userId, Authentication auth) {
        if (!securityUtils.isAdmin(auth)) {
            Long currentUserId = securityUtils.getCurrentUser(auth).getId();
            if (!currentUserId.equals(userId)) {
                throw new UnauthorizedAccessException("You can only view your own bookings");
            }
        }
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id, Authentication auth) {
        if (!securityUtils.isAdmin(auth)) {
            Long currentUserId = securityUtils.getCurrentUser(auth).getId();
            if (!currentUserId.equals(bookingService.getBookingById(id).userId())) {
                throw new UnauthorizedAccessException("You can only cancel your own bookings");
            }
        }
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
