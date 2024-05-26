package com.udea.vuelo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceTest {

    private Price price;

    @BeforeEach
    void setUp() {
        price = new Price(50, 150, 20, 10, 230);
    }

    @Test
    void testPriceConstructorAndGetters() {
        // Arrange
        int seatCost = 50;
        int flightCost = 150;
        int baggageCost = 20;
        int otherCosts = 10;
        int totalCost = 230;

        // Assert
        assertEquals(seatCost, price.getSeatCost());
        assertEquals(flightCost, price.getFlightCost());
        assertEquals(baggageCost, price.getBaggageCost());
        assertEquals(otherCosts, price.getOtherCosts());
        assertEquals(totalCost, price.getTotalCost());
    }

    @Test
    void testPriceSetters() {
        // Arrange
        int seatCost = 60;
        int flightCost = 160;
        int baggageCost = 25;
        int otherCosts = 15;
        int totalCost = 260;

        // Act
        price.setSeatCost(seatCost);
        price.setFlightCost(flightCost);
        price.setBaggageCost(baggageCost);
        price.setOtherCosts(otherCosts);
        price.setTotalCost(totalCost);

        // Assert
        assertEquals(seatCost, price.getSeatCost());
        assertEquals(flightCost, price.getFlightCost());
        assertEquals(baggageCost, price.getBaggageCost());
        assertEquals(otherCosts, price.getOtherCosts());
        assertEquals(totalCost, price.getTotalCost());
    }
}
