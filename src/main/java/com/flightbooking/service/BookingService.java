package com.flightbooking.service;

import com.flightbooking.dto.request.CreateBookingRequest;
import com.flightbooking.dto.response.BookingResponse;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request);
    BookingResponse getBookingById(Long id);
    List<BookingResponse> getBookingsByUser(Long userId);
    BookingResponse cancelBooking(Long id);
    List<BookingResponse> getAllBookings();
}
