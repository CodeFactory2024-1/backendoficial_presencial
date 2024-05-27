package com.udea.vuelo.repository;

import com.udea.vuelo.model.HistoricalReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface HistoricalReservesRepository extends JpaRepository<HistoricalReserve, Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO historico_reservas (flight_id, card_number, transaction_date, total_cost, transaction_status) " +
            "VALUES (:flightId, :cardNumber, :transactionDate, :totalCost, :transactionStatus)", nativeQuery = true)
    void saveTransactionSummary(int flightId, String cardNumber, LocalDateTime transactionDate, BigDecimal totalCost, String transactionStatus);
}
