package com.udea.vuelo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import com.udea.vuelo.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository, ObjectMapper objectMapper) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> searchFlightsByDateRange(LocalDate startDate, LocalDate endDate) {
        return flightRepository.findByDepartureDateBetween(startDate, endDate);
    }

    public List<Flight> searchFlightsByTotalCostRange(int startCost, int endCost) {
        return flightRepository.findByPriceTotalCostBetween(startCost, endCost);
    }

    public List<Flight> searchFlightsByRoute(String origin, String destination) {
        return flightRepository.findByOriginAndDestination(origin, destination);
    }

    public List<Flight> searchFlightsByAirline(String airline) {
        return flightRepository.findByAirline(airline);
    }

    public Optional<Price> searchPriceById(int id) {
        return flightRepository.findPriceById(id);
    }
}
