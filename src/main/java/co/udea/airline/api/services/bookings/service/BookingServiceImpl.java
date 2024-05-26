package co.udea.airline.api.services.bookings.service;

import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.model.jpa.repository.bookings.IBookingRepository;
import co.udea.airline.api.model.jpa.repository.bookings.IPassengerRepository;
import co.udea.airline.api.utils.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService{
    @Autowired
    IBookingRepository bookingRepository;
    @Autowired
    IPassengerRepository passengerRepository;
    @Override
    public List<Passenger> getAllPassengerByBookingId(Long id) {
        if(!bookingRepository.existsById(id)){
            throw new DataNotFoundException("Booking not found.");
        }

        return passengerRepository.findAllByBookingId(id);
    }
}
