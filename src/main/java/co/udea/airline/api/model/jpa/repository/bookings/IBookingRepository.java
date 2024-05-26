package co.udea.airline.api.model.jpa.repository.bookings;

import co.udea.airline.api.model.jpa.model.bookings.Booking;
import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface IBookingRepository extends JpaRepository<Booking,Long> {
    List<Booking>  findAllByFlightId(Long id);
    Booking findByFlightId(Long id);

}
