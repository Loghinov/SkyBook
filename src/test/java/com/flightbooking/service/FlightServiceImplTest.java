package com.flightbooking.service;

import com.flightbooking.dto.request.CreateFlightRequest;
import com.flightbooking.dto.request.SearchFlightRequest;
import com.flightbooking.dto.response.FlightResponse;
import com.flightbooking.entity.Flight;
import com.flightbooking.exception.BookingException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock FlightRepository flightRepository;

    @InjectMocks FlightServiceImpl service;

    private Flight flight;
    private CreateFlightRequest createRequest;

    @BeforeEach
    void setUp() {
        flight = Flight.builder()
                .id(10L).flightNumber("AA101").origin("JFK").destination("LAX")
                .departureTime(LocalDateTime.now().plusDays(3).withHour(8).withMinute(0))
                .arrivalTime(LocalDateTime.now().plusDays(3).withHour(11).withMinute(30))
                .price(new BigDecimal("299.99")).totalSeats(150).availableSeats(150).build();

        createRequest = new CreateFlightRequest(
                "AA101", "JFK", "LAX",
                LocalDateTime.now().plusDays(3).withHour(8).withMinute(0),
                LocalDateTime.now().plusDays(3).withHour(11).withMinute(30),
                new BigDecimal("299.99"), 150
        );
    }

    @Test
    void createFlight_happyPath_returnsResponse() {
        when(flightRepository.findByFlightNumber("AA101")).thenReturn(Optional.empty());
        when(flightRepository.save(any())).thenReturn(flight);

        FlightResponse result = service.createFlight(createRequest);

        assertThat(result.flightNumber()).isEqualTo("AA101");
        assertThat(result.origin()).isEqualTo("JFK");
        assertThat(result.destination()).isEqualTo("LAX");
        assertThat(result.totalSeats()).isEqualTo(150);
        assertThat(result.availableSeats()).isEqualTo(150);
    }

    @Test
    void createFlight_duplicateFlightNumber_throwsBookingException() {
        when(flightRepository.findByFlightNumber("AA101")).thenReturn(Optional.of(flight));

        assertThatThrownBy(() -> service.createFlight(createRequest))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Flight number already exists");
    }

    @Test
    void getFlightById_found_returnsResponse() {
        when(flightRepository.findById(10L)).thenReturn(Optional.of(flight));

        FlightResponse result = service.getFlightById(10L);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.flightNumber()).isEqualTo("AA101");
    }

    @Test
    void getFlightById_notFound_throwsResourceNotFoundException() {
        when(flightRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getFlightById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void searchFlights_returnsMatchingFlights() {
        SearchFlightRequest req = new SearchFlightRequest("JFK", "LAX", LocalDate.now().plusDays(3), 1);
        when(flightRepository.searchFlights(any(), any(), any(), any(), anyInt()))
                .thenReturn(List.of(flight));

        List<FlightResponse> result = service.searchFlights(req);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).origin()).isEqualTo("JFK");
    }

    @Test
    void searchFlightsFlexible_withNullParams_passesNullsToRepository() {
        when(flightRepository.searchFlightsFlexible(isNull(), isNull(), isNull(), isNull(), eq(1)))
                .thenReturn(List.of(flight));

        List<FlightResponse> result = service.searchFlightsFlexible(null, null, null, 1);

        assertThat(result).hasSize(1);
    }

    @Test
    void searchFlightsFlexible_withBlankOrigin_treatsAsNull() {
        when(flightRepository.searchFlightsFlexible(isNull(), eq("LAX"), any(), any(), eq(2)))
                .thenReturn(List.of(flight));

        List<FlightResponse> result = service.searchFlightsFlexible("  ", "LAX", LocalDate.now().plusDays(3), 2);

        assertThat(result).hasSize(1);
    }

    @Test
    void getAllFlights_returnsList() {
        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<FlightResponse> result = service.getAllFlights();

        assertThat(result).hasSize(1);
    }

    @Test
    void deleteFlight_happyPath_callsDeleteById() {
        when(flightRepository.existsById(10L)).thenReturn(true);

        service.deleteFlight(10L);

        verify(flightRepository).deleteById(10L);
    }

    @Test
    void deleteFlight_notFound_throwsResourceNotFoundException() {
        when(flightRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteFlight(999L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(flightRepository, never()).deleteById(any());
    }
}