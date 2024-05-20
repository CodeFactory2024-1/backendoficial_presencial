package co.udea.airline.api.model.jpa.repository.bookings;

import co.udea.airline.api.model.jpa.model.bookings.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookingRepository extends JpaRepository<Booking,Long> {
}
