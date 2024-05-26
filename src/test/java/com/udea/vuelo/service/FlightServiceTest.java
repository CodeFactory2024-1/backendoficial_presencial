package com.udea.vuelo.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.assertj.core.api.Assertions;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class FlightServiceTest {

    private FlightService flightService;

    private Flight[]  flights;

    @Mock
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        flightService = new FlightService(objectMapperMock);

        // Crear algunos objetos Flight de ejemplo
        Flight flight1 = new Flight(1, "Airline1", "Origin1", "Destination1", new Price(50, 150, 20, 10, 230), LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 2));
        Flight flight2 = new Flight(2, "Airline2", "Origin2", "Destination2", new Price(50, 150, 20, 10, 230), LocalDate.of(2024, 4, 3), LocalDate.of(2024, 4, 4));
        Flight flight3 = new Flight(3, "Airline1", "Origin3", "Destination3", new Price(50, 150, 20, 10, 230), LocalDate.of(2025, 4, 5), LocalDate.of(2025, 4, 6));

        // Agregar los objetos Flight a un array
        flights = new Flight[]{flight1, flight2, flight3}; // Corrección aquí
    }

    @Test

    void testSearchFlightsByDate() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 10);

        // Simular el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con las fechas de inicio y fin dadas
        try {
            // Mocking the behavior of objectMapperMock
            when(objectMapperMock.readValue(Mockito.any(InputStream.class), Mockito.eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            throw new IllegalStateException (e.getMessage(), e);
        }

        // Act
        List<List<Flight>> result = flightService.searchFlightsByDate(startDate, endDate);


        // Assert
        assertNotNull(result);
        assertEquals(2, result.get(0).size());

    }

    @Test
    void testSearchFlightsByTotalCost(){
        // Arrange
        int startCost = 100;
        int endCost = 300;

        try {
            // Mocking the behavior of objectMapperMock
            when(objectMapperMock.readValue(Mockito.any(InputStream.class), Mockito.eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            // Handle exception
        }
        // Act
        List<List<Flight>> result = flightService.searchFlightsByTotalCost(startCost, endCost);

        result.stream().flatMap(List::stream).forEach(flight -> {
            System.out.println("Flight ID: " + flight.getDestination());
            // Print other flight details as needed
        });

        // Assert
         //Add your assertions here
        assertNotNull(result);
        assertEquals(2, result.size());
    }


    @Test
    void testSearchFlightsByRoute() {
        // Arrange
        String origin = "Origin1";
        String destination = "Destination1";


        try {
            // Mocking the behavior of objectMapperMock
            when(objectMapperMock.readValue(Mockito.any(InputStream.class), Mockito.eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            // Handle exception
        }

        // Act
        List<List<Flight>> result = flightService.searchFlightsByRoute(origin, destination);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(flights[0], result.get(0).get(0));
    }


    @Test
    void testSearchFlightsByAirline() {
        // Arrange
        String airline = "Airline1";


        try {
            // Mocking the behavior of objectMapperMock
            when(objectMapperMock.readValue(Mockito.any(InputStream.class), Mockito.eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            // Handle exception
        }

        // Act
        List<List<Flight>> result = flightService.searchFlightsByAirline(airline);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.get(0).size());
    }

    @Test
    void testSearchPriceById() {
        // Arrange
        int id = 1;


        try {
            // Mocking the behavior of objectMapperMock
            when(objectMapperMock.readValue(Mockito.any(InputStream.class), Mockito.eq(Flight[].class))).thenReturn(flights);
        } catch (IOException e) {
            // Handle exception
        }

        // Act
        Price result = flightService.searchPriceById(id);

        // Assert
        assertNotNull(result);
        assertEquals(flights[0].getPrice(), result);
    }


    @Test
    void testJsonReadException() {
        // Arrange
        String errorMessage = "Error de lectura JSON";
        Throwable cause = new RuntimeException("Causa de la excepción");

        // Act
        JsonReadException exception = new JsonReadException(errorMessage, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }


}
