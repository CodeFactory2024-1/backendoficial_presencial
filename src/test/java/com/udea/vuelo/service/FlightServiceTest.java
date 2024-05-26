package com.udea.vuelo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import com.udea.vuelo.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class FlightServiceTest {

    private FlightService flightService;

    private Flight[] flights;

    @Mock
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        flightService = new FlightService((FlightRepository) objectMapperMock);

        // Crear algunos objetos Flight de ejemplo
        Flight flight1 = new Flight(1, "Airline1", "Origin1", "Destination1", new Price(50, 150, 20, 10, 230), LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 2));
        Flight flight2 = new Flight(2, "Airline2", "Origin2", "Destination2", new Price(50, 150, 20, 10, 230), LocalDate.of(2024, 4, 3), LocalDate.of(2024, 4, 4));
        Flight flight3 = new Flight(3, "Airline1", "Origin3", "Destination3", new Price(50, 150, 20, 10, 230), LocalDate.of(2025, 4, 5), LocalDate.of(2025, 4, 6));

        flights = new Flight[]{flight1, flight2, flight3};
    }

    @Test
    void testSearchFlightsByDate() {
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 10);

        try {
            when(objectMapperMock.readValue(any(InputStream.class), eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        List<Flight> result = flightService.searchFlightsByDateRange(startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchFlightsByTotalCost() {
        int startCost = 100;
        int endCost = 300;

        try {
            when(objectMapperMock.readValue(any(InputStream.class), eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        List<Flight> result = flightService.searchFlightsByTotalCostRange(startCost, endCost);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testSearchFlightsByRoute() {
        String origin = "Origin1";
        String destination = "Destination1";

        try {
            when(objectMapperMock.readValue(any(InputStream.class), eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        List<Flight> result = flightService.searchFlightsByRoute(origin, destination);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(flights[0], result.get(0));
    }

    @Test
    void testSearchFlightsByAirline() {
        String airline = "Airline1";

        try {
            when(objectMapperMock.readValue(any(InputStream.class), eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        List<Flight> result = flightService.searchFlightsByAirline(airline);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchPriceById() {
        int id = 1;

        try {
            when(objectMapperMock.readValue(any(InputStream.class), eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        Optional<Price> result = flightService.searchPriceById(id);

        assertNotNull(result);
        assertEquals(flights[0].getPrice(), result);
    }

}
