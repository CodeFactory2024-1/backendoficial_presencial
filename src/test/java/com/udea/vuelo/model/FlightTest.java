package com.udea.vuelo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FlightTest {

    @Mock
    private Price priceMock;

    private Flight flight;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        flight = new Flight(1, "Airline1", "Origin1", "Destination1", priceMock, LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 4, 2));
    }

    @Test
    void testFlightConstructorAndGetters() {
        // Arrange
        int id = 1;
        String airline = "Airline1";
        String origin = "Origin1";
        String destination = "Destination1";
        LocalDate departureDate = LocalDate.of(2024, 4, 1);
        LocalDate arrivalDate = LocalDate.of(2024, 4, 2);

        // Assert
        assertEquals(id, flight.getId());
        assertEquals(airline, flight.getAirline());
        assertEquals(origin, flight.getOrigin());
        assertEquals(destination, flight.getDestination());
        assertEquals(priceMock, flight.getPrice());
        assertEquals(departureDate, flight.getDepartureDate());
        assertEquals(arrivalDate, flight.getArrivalDate());
    }

    @Test
    void testFlightSetters() {
        // Arrange
        int id = 2;
        String airline = "Airline2";
        String origin = "Origin2";
        String destination = "Destination2";
        LocalDate departureDate = LocalDate.of(2024, 4, 3);
        LocalDate arrivalDate = LocalDate.of(2024, 4, 4);

        // Act
        flight.setId(id);
        flight.setAirline(airline);
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setPrice(priceMock);
        flight.setDepartureDate(departureDate);
        flight.setArrivalDate(arrivalDate);

        // Assert
        assertEquals(id, flight.getId());
        assertEquals(airline, flight.getAirline());
        assertEquals(origin, flight.getOrigin());
        assertEquals(destination, flight.getDestination());
        assertEquals(priceMock, flight.getPrice());
        assertEquals(departureDate, flight.getDepartureDate());
        assertEquals(arrivalDate, flight.getArrivalDate());
    }

    @Test
    void testFlightNoArgsConstructor() {
        // Arrange
        Flight emptyFlight = new Flight();

        // Assert
        assertNotNull(emptyFlight);
    }
}