package com.flightbooking.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.flightbooking.dto.response.FlightResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@ConditionalOnProperty(name = "amadeus.enabled", havingValue = "true")
public class AmadeusService {

    private final Amadeus amadeus;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public AmadeusService(
            @Value("${amadeus.client-id}") String clientId,
            @Value("${amadeus.client-secret}") String clientSecret) {
        this.amadeus = Amadeus.builder(clientId, clientSecret).build();
    }

    public List<FlightResponse> searchFlights(String origin, String destination,
                                               String date, int adults) {
        try {
            FlightOfferSearch[] offers = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", origin.toUpperCase())
                          .and("destinationLocationCode", destination.toUpperCase())
                          .and("departureDate", date)
                          .and("adults", String.valueOf(adults))
                          .and("max", "10")
                          .and("currencyCode", "USD")
            );
            return Arrays.stream(offers)
                    .map(this::toFlightResponse)
                    .toList();
        } catch (ResponseException e) {
            return Collections.emptyList();
        }
    }

    private FlightResponse toFlightResponse(FlightOfferSearch offer) {
        FlightOfferSearch.Itinerary itin  = offer.getItineraries()[0];
        FlightOfferSearch.SearchSegment[] segs = itin.getSegments();
        FlightOfferSearch.SearchSegment first = segs[0];
        FlightOfferSearch.SearchSegment last  = segs[segs.length - 1];

        String flightNum = first.getCarrierCode() + first.getNumber();
        String depStr    = first.getDeparture().getAt();
        String arrStr    = last.getArrival().getAt();

        // Amadeus may include offset — strip to 19 chars if longer
        LocalDateTime dep = LocalDateTime.parse(depStr.length() > 19 ? depStr.substring(0, 19) : depStr, FMT);
        LocalDateTime arr = LocalDateTime.parse(arrStr.length() > 19 ? arrStr.substring(0, 19) : arrStr, FMT);

        int seats = offer.getNumberOfBookableSeats();

        return new FlightResponse(
                null,
                flightNum,
                first.getDeparture().getIataCode(),
                last.getArrival().getIataCode(),
                dep,
                arr,
                new BigDecimal(offer.getPrice().getGrandTotal()),
                seats,
                seats
        );
    }
}
