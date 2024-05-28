package co.udea.airline.api.services.bookings.service;

import co.udea.airline.api.model.DTO.BookingDTO;
import co.udea.airline.api.model.DTO.PassengerDTO;
import java.util.List;

public interface IBookingService {
    List<PassengerDTO> getAllPassengerByBookingId(Long id);
    BookingDTO getBookingById(Long id);
}
