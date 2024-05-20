package co.udea.airline.api.model.jpa.repository.seats;

import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.jpa.model.seats.SeatXPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISeatXPassengerRepository extends JpaRepository<SeatXPassenger, Long> {
    Boolean existsBySeatId(Long id);
    Boolean existsByPassengerId(Long id);
    Boolean existsBySeatIdAndPassengerId(Long seatId, Long passengerId);
    SeatXPassenger findSeatXPassengerBySeatIdAndPassengerId(Long seatId, Long passengerId);
}
