package com.udea.vuelo.repository;

import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    List<Flight> findByDepartureDateBetween(LocalDate startDate, LocalDate endDate);

    List<Flight> findByPriceTotalCostBetween(int minTotalCost, int maxTotalCost);

    List<Flight> findByOriginAndDestination(String origin, String destination);

    List<Flight> findByAirline(String airline);

    @Query("SELECT f.price FROM Flight f WHERE f.id = :id")
    Optional<Price> findPriceById(@Param("id") int id);
}
