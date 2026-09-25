package com.flightbooking.mapper;

import com.flightbooking.dto.response.BookingResponse;
import com.flightbooking.entity.Booking;

public class BookingMapper {
    private BookingMapper() {}

    public static BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getFirstName() + " " + booking.getUser().getLastName(),
                booking.getFlight().getId(),
                booking.getFlight().getFlightNumber(),
                booking.getStatus(),
                booking.getNumberOfPassengers(),
                booking.getTotalPrice(),
                booking.getBookedAt()
        );
    }
}
