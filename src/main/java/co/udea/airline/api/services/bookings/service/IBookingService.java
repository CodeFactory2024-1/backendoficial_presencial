package co.udea.airline.api.services.bookings.service;

import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import java.util.List;

public interface IBookingService {
    List<Passenger> getAllPassengerByBookingId(Long id);
}
