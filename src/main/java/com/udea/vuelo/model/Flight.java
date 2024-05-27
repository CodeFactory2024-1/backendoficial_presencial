package com.udea.vuelo.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String airline;
    private String origin;
    private String destination;

    @Embedded
    private Price price;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate departureDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate arrivalDate;

    public Flight() {
    }

    public Flight(int id, String airline, String origin, String destination, Price price, LocalDate departureDate, LocalDate arrivalDate) {
        this.id = id;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.price = price;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
