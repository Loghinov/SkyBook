package com.flightbooking.controller;

import com.flightbooking.dto.request.CreateFlightRequest;
import com.flightbooking.dto.request.SearchFlightRequest;
import com.flightbooking.dto.response.FlightResponse;
import com.flightbooking.service.AmadeusService;
import com.flightbooking.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @Autowired(required = false)
    private AmadeusService amadeusService;

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@Valid @RequestBody CreateFlightRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightService.createFlight(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @PostMapping("/search")
    public ResponseEntity<List<FlightResponse>> searchFlights(@Valid @RequestBody SearchFlightRequest request) {
        return ResponseEntity.ok(flightService.searchFlights(request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponse>> searchFlightsGet(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") int passengers) {
        return ResponseEntity.ok(flightService.searchFlightsFlexible(origin, destination, date, passengers));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/live-search")
    public ResponseEntity<List<FlightResponse>> liveSearch(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam(defaultValue = "1") int adults) {
        if (amadeusService == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(amadeusService.searchFlights(origin, destination, date, adults));
    }

    @GetMapping("/amadeus-enabled")
    public ResponseEntity<Boolean> isAmadeusEnabled() {
        return ResponseEntity.ok(amadeusService != null);
    }
}
