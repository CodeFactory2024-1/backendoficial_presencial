package co.udea.airline.api.services.bookings.service;

import co.udea.airline.api.model.DTO.BookingDTO;
import co.udea.airline.api.model.DTO.PassengerDTO;
import co.udea.airline.api.model.jpa.model.bookings.Booking;
import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.model.jpa.repository.bookings.IBookingRepository;
import co.udea.airline.api.model.jpa.repository.bookings.IPassengerRepository;
import co.udea.airline.api.model.mapper.BookingMapper;
import co.udea.airline.api.model.mapper.ICustomMapper;
import co.udea.airline.api.model.mapper.PassengerMapper;
import co.udea.airline.api.utils.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements IBookingService{
    @Autowired
    IBookingRepository bookingRepository;
    @Autowired
    IPassengerRepository passengerRepository;
    @Autowired
    PassengerMapper passengerMapper;
    @Autowired
    BookingMapper bookingMapper;

    @Override
    public List<PassengerDTO> getAllPassengerByBookingId(Long id) {
        if(!bookingRepository.existsById(id)){
            throw new DataNotFoundException("Booking not found.");
        }
        List<Passenger> passengerList = passengerRepository.findAllByBookingId(id);
        return passengerList.stream().map(passengerMapper::convertToDto).toList();
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if(bookingOptional.isEmpty()){
            throw new DataNotFoundException("Booking not found");
        }
        return bookingMapper.convertToDto(bookingOptional.get());
    }
}
