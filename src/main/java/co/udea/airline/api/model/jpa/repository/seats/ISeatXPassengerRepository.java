package co.udea.airline.api.model.jpa.repository.seats;

import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.jpa.model.seats.SeatXPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ISeatXPassengerRepository extends JpaRepository<SeatXPassenger, Long> {
    Boolean existsBySeatId(Long id);
    Boolean existsByPassengerId(Long id);
    Boolean existsBySeatIdAndPassengerId(Long seatId, Long passengerId);
    Optional<SeatXPassenger> findBySeatIdAndPassengerId(Long seatId, Long passengerId);
    Optional<SeatXPassenger> findByPassengerId(Long passengerId);

    @Modifying
    @Query("DELETE FROM SeatXPassenger sp WHERE sp.id=:id")
    void deleteSeatPassenger(Long id);
}
