package co.udea.airline.api.model.jpa.repository;

import co.udea.airline.api.model.jpa.model.Booking_Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingPassengerRepository extends JpaRepository<Booking_Passenger, Long> {
    @Query("SELECT bp FROM Booking_Passenger bp WHERE bp.passenger.passengerId = ?1")
    List<Booking_Passenger> findByPassengerPassengerId(Long passengerId);

    @Query("SELECT bp FROM Booking_Passenger bp WHERE bp.booking.bookingId = ?1")
    List<Booking_Passenger> findByBookingBookingId(Long bookingId);
}
