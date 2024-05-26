package co.udea.airline.api.service;

import co.udea.airline.api.model.jpa.model.Booking;
import co.udea.airline.api.model.jpa.model.Booking_Passenger;
import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.model.jpa.repository.BookingPassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingPassengerService {
    @Autowired
    BookingPassengerRepository bookingPassengerRepository;

    public List<Booking_Passenger> getBookingsPassengers() {
        return bookingPassengerRepository.findAll();
    }

    public Optional<Booking_Passenger> getBookingPassenger(Long id) {
        return bookingPassengerRepository.findById(id);
    }

    public void saveOrUpdate(Booking_Passenger booking_passenger){
        bookingPassengerRepository.save(booking_passenger);
    }

    public List<Booking> getBookingPassengersByPassengerId(Long passengerId){
        return bookingPassengerRepository.findByPassengerPassengerId(passengerId)
                .stream()
                .map(Booking_Passenger::getBooking)
                .toList();
    }

    public List<Passenger> getBookingPassengersByBookingId(Long bookingId) {
        return bookingPassengerRepository.findByBookingBookingId(bookingId)
                .stream()
                .map(Booking_Passenger::getPassenger)
                .toList();
    }
}
