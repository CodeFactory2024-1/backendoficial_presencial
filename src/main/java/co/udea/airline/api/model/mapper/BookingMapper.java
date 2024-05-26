package co.udea.airline.api.model.mapper;

import co.udea.airline.api.model.DTO.BookingDTO;
import co.udea.airline.api.model.jpa.model.bookings.Booking;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper implements ICustomMapper<Booking, BookingDTO> {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IFlightRepository flightRepository;

    @Override
    public BookingDTO convertToDto(Booking entity) {
        return modelMapper.map(entity, BookingDTO.class);
    }

    @Override
    public Booking convertToEntity(BookingDTO entityDTO) {
        return modelMapper.map(entityDTO, Booking.class);
    }
}
