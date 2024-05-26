package com.udea.vuelo.controller;

import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import com.udea.vuelo.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class FlightControllerTest {

    @Mock
    private FlightService flightServiceMock;

    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        flightController = new FlightController(flightServiceMock);
    }

    @Test
    void testSearchFlightsByDate() {
        // Arrange
        String startDate = "2024-04-01";
        String endDate = "2024-04-10";
        LocalDate parsedStartDate = LocalDate.parse(startDate);
        LocalDate parsedEndDate = LocalDate.parse(endDate);
        List<Flight> expectedFlights = Arrays.asList(new Flight(), new Flight());

        // Configurar el comportamiento del mock
        when(flightServiceMock.searchFlightsByDateRange(parsedStartDate, parsedEndDate)).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightController.searchFlightsByDate(startDate, endDate);

        // Assert
        assertEquals(expectedFlights, result);
    }

    @Test
    void testSearchFlightsByPrice() {
        // Arrange
        int startPrice = 100;
        int endPrice = 300;
        List<Flight> expectedFlights = Arrays.asList(new Flight(), new Flight());

        // Configurar el comportamiento del mock
        when(flightServiceMock.searchFlightsByTotalCostRange(startPrice, endPrice)).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightController.searchFlightsByPrice(startPrice, endPrice);

        // Assert
        assertEquals(expectedFlights, result);
    }

    @Test
    void testSearchFlightsByRoute() {
        // Arrange
        String origin = "Origin1";
        String destination = "Destination1";
        List<Flight> expectedFlights = Arrays.asList(new Flight());

        // Configurar el comportamiento del mock
        when(flightServiceMock.searchFlightsByRoute(origin, destination)).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightController.searchFlightsByRoute(origin, destination);

        // Assert
        assertEquals(expectedFlights, result);
    }

    @Test
    void testSearchFlightsByAirline() {
        // Arrange
        String airline = "Airline1";
        List<Flight> expectedFlights = Arrays.asList(new Flight(), new Flight());

        // Configurar el comportamiento del mock
        when(flightServiceMock.searchFlightsByAirline(airline)).thenReturn(expectedFlights);

        // Act
        List<Flight> result = flightController.searchFlightsByAirline(airline);

        // Assert
        assertEquals(expectedFlights, result);
    }

    @Test
    void testPriceById() {
        // Arrange
        int id = 1;
        Price expectedPrice = new Price(); // Supongamos que el servicio devuelve un precio.

        // Configurar el comportamiento del mock
        when(flightServiceMock.searchPriceById(id)).thenReturn(Optional.of(expectedPrice));

        // Act
        ResponseEntity<Price> result = flightController.priceById(id);

        // Assert
        assertEquals(expectedPrice, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testPaymentGateways() {
        // Arrange
        List<String> expectedGateways = Arrays.asList("PayPal", "Pse", "Wompi", "Bancolombia", "GetTrx", "Stripe", "PayU", "Mercadopago");

        // Act
        ResponseEntity<List<String>> result = flightController.paymentGateways();

        // Assert
        assertEquals(expectedGateways, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
