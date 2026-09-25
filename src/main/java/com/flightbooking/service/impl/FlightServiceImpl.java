package com.flightbooking.service.impl;

import com.flightbooking.dto.request.CreateFlightRequest;
import com.flightbooking.dto.request.SearchFlightRequest;
import com.flightbooking.dto.response.FlightResponse;
import com.flightbooking.entity.Flight;
import com.flightbooking.exception.BookingException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.mapper.FlightMapper;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    @Transactional
    public FlightResponse createFlight(CreateFlightRequest request) {
        if (flightRepository.findByFlightNumber(request.flightNumber()).isPresent()) {
            throw new BookingException("Flight number already exists: " + request.flightNumber());
        }
        Flight flight = Flight.builder()
                .flightNumber(request.flightNumber())
                .origin(request.origin())
                .destination(request.destination())
                .departureTime(request.departureTime())
                .arrivalTime(request.arrivalTime())
                .price(request.price())
                .totalSeats(request.totalSeats())
                .availableSeats(request.totalSeats())
                .build();
        return FlightMapper.toResponse(flightRepository.save(flight));
    }

    @Override
    public FlightResponse getFlightById(Long id) {
        return FlightMapper.toResponse(findFlightById(id));
    }

    @Override
    public List<FlightResponse> searchFlights(SearchFlightRequest request) {
        LocalDateTime from = request.departureDate().atStartOfDay();
        LocalDateTime to = request.departureDate().plusDays(1).atStartOfDay();
        return flightRepository.searchFlights(
                request.origin(), request.destination(), from, to, request.passengers()
        ).stream().map(FlightMapper::toResponse).toList();
    }

    @Override
    public List<FlightResponse> searchFlightsFlexible(String origin, String destination, LocalDate date, int passengers) {
        LocalDateTime from = date != null ? date.atStartOfDay() : null;
        LocalDateTime to   = date != null ? date.plusDays(1).atStartOfDay() : null;
        String originParam = (origin != null && !origin.isBlank()) ? origin : null;
        String destParam   = (destination != null && !destination.isBlank()) ? destination : null;
        return flightRepository.searchFlightsFlexible(originParam, destParam, from, to, passengers)
                .stream().map(FlightMapper::toResponse).toList();
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(FlightMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }

    Flight findFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
    }
}
