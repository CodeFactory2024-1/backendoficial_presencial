package com.udea.vuelo.service;

import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import com.udea.vuelo.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
    private FlightRepository flightRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flightService = new FlightService(flightRepositoryMock);

        // Crear algunos objetos Flight de ejemplo
        Flight flight1 = new Flight(1, "Airline1", "Origin1", "Destination1", new Price(50, 150, 20, 10, 230), LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 2));
        Flight flight2 = new Flight(2, "Airline2", "Origin2", "Destination2", new Price(50, 150, 20, 10, 230), LocalDate.of(2024, 4, 3), LocalDate.of(2024, 4, 4));
        Flight flight3 = new Flight(3, "Airline1", "Origin3", "Destination3", new Price(50, 150, 20, 10, 230), LocalDate.of(2025, 4, 5), LocalDate.of(2025, 4, 6));

        // Agregar los objetos Flight a un array
        flights = new Flight[]{flight1, flight2, flight3};
    }

    @Test
    void testSearchFlightsByDate() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 10);

        // Simular el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con las fechas de inicio y fin dadas
        when(flightRepositoryMock.findByDepartureDateBetween(startDate, endDate)).thenReturn(List.of(flights[0], flights[1]));

        // Act
        List<Flight> result = flightService.searchFlightsByDateRange(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchFlightsByTotalCost(){
        // Arrange
        int startCost = 100;
        int endCost = 300;

        // Simular el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con los costos totales dados
        when(flightRepositoryMock.findByPriceTotalCostBetween(startCost, endCost)).thenReturn(List.of(flights[0], flights[1]));

        // Act
        List<Flight> result = flightService.searchFlightsByTotalCostRange(startCost, endCost);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchFlightsByRoute() {
        // Arrange
        String origin = "Origin1";
        String destination = "Destination1";

        // Simular el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con el origen y destino dados
        when(flightRepositoryMock.findByOriginAndDestination(origin, destination)).thenReturn(List.of(flights[0]));

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
        String airline = "Airline1";

        // Simular el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con la aerolínea dada
        when(flightRepositoryMock.findByAirline(airline)).thenReturn(List.of(flights[0], flights[2]));

        // Act
        List<Flight> result = flightService.searchFlightsByAirline(airline);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
