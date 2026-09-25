package com.flightbooking.mapper;

import com.flightbooking.dto.response.FlightResponse;
import com.flightbooking.entity.Flight;

public class FlightMapper {
    private FlightMapper() {}

    public static FlightResponse toResponse(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice(),
                flight.getTotalSeats(),
                flight.getAvailableSeats()
        );
    }
}
