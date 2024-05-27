package com.udea.vuelo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_reservas")
public class HistoricalReserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "flight_id")
    private int flightId;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "transaction_status")
    private String transactionStatus;

    // Constructor, getters, and setters
}
