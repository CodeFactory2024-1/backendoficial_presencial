package com.udea.vuelo.service;

import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import com.udea.vuelo.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepositoryMock;

    private Flight[] flights;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear algunos objetos Flight de ejemplo
        Price price1 = new Price(50, 150, 20, 10, 230);
        Price price2 = new Price(50, 150, 20, 10, 230);
        Price price3 = new Price(50, 150, 20, 10, 230);

        Flight flight1 = new Flight(1, "Airline1", "Origin1", "Destination1", price1, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 2));
        Flight flight2 = new Flight(2, "Airline2", "Origin2", "Destination2", price2, LocalDate.of(2024, 4, 3), LocalDate.of(2024, 4, 4));
        Flight flight3 = new Flight(3, "Airline1", "Origin3", "Destination3", price3, LocalDate.of(2025, 4, 5), LocalDate.of(2025, 4, 6));

        // Agregar los objetos Flight a un array
        flights = new Flight[]{flight1, flight2, flight3};
    }

    @Test
    void testSearchFlightsByDate() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 5);

        when(flightRepositoryMock.findAll()).thenReturn(List.of(flights));

        // Act
        List<Flight> result = flightService.searchFlightsByDateRange(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchFlightsByTotalCost() {
        // Arrange
        int startCost = 100;
        int endCost = 300;

        when(flightRepositoryMock.findAll()).thenReturn(List.of(flights));

        // Act
        List<Flight> result = flightService.searchFlightsByTotalCostRange(startCost, endCost);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testSearchFlightsByRoute() {
        // Arrange
        String origin = "Origin1";
        String destination = "Destination1";

        when(flightRepositoryMock.findAll()).thenReturn(List.of(flights));

        // Act
        List<Flight> result = flightService.searchFlightsByRoute(origin, destination);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(flights[0], result.get(0));
    }

    @Test
    void testSearchFlightsByAirline() {
        // Arrange
        String airline = "Airline2";

        when(flightRepositoryMock.findAll()).thenReturn(List.of(flights));

        // Act
        List<Flight> result = flightService.searchFlightsByAirline(airline);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchPriceById() {
        // Arrange
        int id = 1;

        when(flightRepositoryMock.findById(id)).thenReturn(Optional.of(flights[0]));

        // Act
        Optional<Price> result = flightService.searchPriceById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(flights[0].getPrice(), result.get());
    }
}
