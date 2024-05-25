package com.udea.vuelo.controller;

import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import com.udea.vuelo.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/searchbydate")
    public List<Flight> searchFlightsByDate(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate) {
        LocalDate parsedStartDate = LocalDate.parse(startDate);
        LocalDate parsedEndDate = LocalDate.parse(endDate);
        return flightService.searchFlightsByDateRange(parsedStartDate, parsedEndDate);
    }

    @GetMapping("/searchbyprice")
    public List<Flight> searchFlightsByPrice(
            @RequestParam(name = "startPrice") int startPrice,
            @RequestParam(name = "endPrice") int endPrice) {
        return flightService.searchFlightsByTotalCostRange(startPrice, endPrice);
    }

    @GetMapping("/searchbyroute")
    public List<Flight> searchFlightsByRoute(
            @RequestParam(name = "origin") String origin,
            @RequestParam(name = "destination") String destination) {
        return flightService.searchFlightsByRoute(origin, destination);
    }

    @GetMapping("/searchbyairline")
    public List<Flight> searchFlightsByAirline(
            @RequestParam(name = "airline") String airline) {
        return flightService.searchFlightsByAirline(airline);
    }

    @GetMapping("/pricebyid")
    public ResponseEntity<Price> priceById(@RequestParam(name = "id") int id) {
        return flightService.searchPriceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paymentgateways")
    public ResponseEntity<List<String>> paymentGateways() {
        List<String> paymentGateways = List.of("PayPal", "Pse", "Wompi", "Bancolombia", "GetTrx", "Stripe", "PayU", "Mercadopago");
        return ResponseEntity.ok(paymentGateways);
    }
}
