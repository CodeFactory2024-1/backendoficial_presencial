package co.udea.airline.api.model.mapper;

import co.udea.airline.api.model.DTO.CreateSeatDTO;
import co.udea.airline.api.model.DTO.PassengerDTO;
import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper implements ICustomMapper<Passenger, PassengerDTO> {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IFlightRepository flightRepository;
    @Override
    public PassengerDTO convertToDto(Passenger entity) {
        return  modelMapper.map(entity, PassengerDTO.class);
    }
    @Override
    public Passenger convertToEntity(PassengerDTO entityDTO) {
        return modelMapper.map(entityDTO, Passenger.class);
    }
}
