package com.udea.vuelo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import com.udea.vuelo.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class FlightControllerTest {
    @Mock
    private ObjectMapper objectMapperMock;

    private Flight[]  flights;
    @Mock
    private FlightService flightServiceMock;

    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        flightController = new FlightController(objectMapperMock, flightServiceMock);

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
        String startDate = "2024-04-01";
        String endDate = "2024-04-10";

        List<List<Flight>> expectedFlights = Arrays.asList(Arrays.asList(flights[0], flights[1]));

        // Simular el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con las fechas de inicio y fin dadas
        when(flightServiceMock.searchFlightsByDate(LocalDate.parse(startDate), LocalDate.parse(endDate))).thenReturn(expectedFlights);

        // Act
        List<List<Flight>> result = flightController.searchFlightsByDate(startDate, endDate);

        // Assert
        assertEquals(expectedFlights, result);
    }

    @Test
    void testSearchFlightsByPrice() {
        // Arrange
        int startPrice = 100;
        int endPrice = 300;

        List<List<Flight>> expectedFlights = Arrays.asList(
                Arrays.asList(flights[0]),
                Arrays.asList(flights[1]),
                Arrays.asList(flights[2])
        );

        // Configurar el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con los precios de inicio y fin proporcionados.
        when(flightServiceMock.searchFlightsByTotalCost(startPrice, endPrice)).thenReturn(expectedFlights);

        // Act
        List<List<Flight>> result = flightController.searchFlightsByPrice(startPrice, endPrice);

        // Assert
        assertEquals(expectedFlights, result);
    }

    @Test
    void testSearchFlightsByRoute() {
        // Arrange
        String origin = "Origin1";
        String destination = "Destination1";

        List<List<Flight>> expectedFlights = Arrays.asList(
                Arrays.asList(flights[0])  // Vuelo con origen y destino proporcionados
        );

        // Configurar el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con el origen y destino proporcionados.
        when(flightServiceMock.searchFlightsByRoute(origin, destination)).thenReturn(expectedFlights);

        // Act
        List<List<Flight>> result = flightController.searchFlightsByRoute(origin, destination);

        // Assert
        assertEquals(expectedFlights, result);
    }

    @Test
    void testSearchFlightsByAirline() {
        // Arrange
        String airline = "Airline1";

        // Lista de vuelos esperados para la aerolínea proporcionada
        List<Flight> expectedFlights = Arrays.asList(flights[0]);

        // Configurar el comportamiento del mock para que devuelva los vuelos esperados cuando se le llame con la aerolínea proporcionada.
        when(flightServiceMock.searchFlightsByAirline(airline)).thenReturn(Arrays.asList(expectedFlights));

        // Act
        List<List<Flight>> result = flightController.searchFlightsByAirline(airline);

        // Assert
        assertEquals(Arrays.asList(expectedFlights), result);
    }

    @Test
    void testPriceById() {
        // Arrange
        int id = 1;
        Price expectedPrice = flights[0].getPrice(); // Suponiendo que flight1 tiene el ID 1 y su precio es el esperado.

        // Configurar el comportamiento del mock para que devuelva el precio esperado cuando se le llame con el ID proporcionado.
        when(flightServiceMock.searchPriceById(id)).thenReturn(expectedPrice);

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
        ResponseEntity<List<String>> result = flightController.paymentgateways();

        // Assert
        assertEquals(expectedGateways, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }





}