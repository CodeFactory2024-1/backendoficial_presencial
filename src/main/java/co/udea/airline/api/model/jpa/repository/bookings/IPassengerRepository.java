package co.udea.airline.api.model.jpa.repository.bookings;

import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPassengerRepository extends JpaRepository<Passenger,Long> {
    List<Passenger> findAllByBookingId(Long bookingId);
}
