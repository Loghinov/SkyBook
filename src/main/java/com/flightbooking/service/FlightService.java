package com.flightbooking.service;

import com.flightbooking.dto.request.CreateFlightRequest;
import com.flightbooking.dto.request.SearchFlightRequest;
import com.flightbooking.dto.response.FlightResponse;
import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    FlightResponse createFlight(CreateFlightRequest request);
    FlightResponse getFlightById(Long id);
    List<FlightResponse> searchFlights(SearchFlightRequest request);
    List<FlightResponse> searchFlightsFlexible(String origin, String destination, LocalDate date, int passengers);
    List<FlightResponse> getAllFlights();
    void deleteFlight(Long id);
}
