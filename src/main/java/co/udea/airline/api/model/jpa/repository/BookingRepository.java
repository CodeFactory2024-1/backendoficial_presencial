package co.udea.airline.api.model.jpa.repository;

import co.udea.airline.api.model.jpa.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.flightId = ?1")
    List<Booking> findBookingByFlightId(Long flightId);
}
