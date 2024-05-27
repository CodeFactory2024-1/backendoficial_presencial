package com.udea.vuelo.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Price {
    private int seatCost;
    private int flightCost;
    private int baggageCost;
    private int otherCosts;
    private int totalCost; // Cambiado de double a int

    public Price() {
    }

    public Price(int seatCost, int flightCost, int baggageCost, int otherCosts, int totalCost) {
        this.seatCost = seatCost;
        this.flightCost = flightCost;
        this.baggageCost = baggageCost;
        this.otherCosts = otherCosts;
        this.totalCost = totalCost;
    }

    public int getSeatCost() {
        return seatCost;
    }

    public void setSeatCost(int seatCost) {
        this.seatCost = seatCost;
    }

    public int getFlightCost() {
        return flightCost;
    }

    public void setFlightCost(int flightCost) {
        this.flightCost = flightCost;
    }

    public int getBaggageCost() {
        return baggageCost;
    }

    public void setBaggageCost(int baggageCost) {
        this.baggageCost = baggageCost;
    }

    public int getOtherCosts() {
        return otherCosts;
    }

    public void setOtherCosts(int otherCosts) {
        this.otherCosts = otherCosts;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
