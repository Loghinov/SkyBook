package com.flightbooking.repository;

import com.flightbooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE f.origin = :origin AND f.destination = :destination " +
           "AND f.departureTime >= :from AND f.departureTime < :to AND f.availableSeats >= :passengers")
    List<Flight> searchFlights(@Param("origin") String origin,
                               @Param("destination") String destination,
                               @Param("from") LocalDateTime from,
                               @Param("to") LocalDateTime to,
                               @Param("passengers") int passengers);

    @Query("SELECT f FROM Flight f WHERE " +
           "(:origin IS NULL OR LOWER(f.origin) LIKE LOWER(CONCAT('%',:origin,'%'))) AND " +
           "(:destination IS NULL OR LOWER(f.destination) LIKE LOWER(CONCAT('%',:destination,'%'))) AND " +
           "(:from IS NULL OR f.departureTime >= :from) AND " +
           "(:to IS NULL OR f.departureTime < :to) AND " +
           "f.availableSeats >= :passengers")
    List<Flight> searchFlightsFlexible(@Param("origin") String origin,
                                       @Param("destination") String destination,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to,
                                       @Param("passengers") int passengers);
}