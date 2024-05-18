package co.udea.airline.api.model.mapper;

import co.udea.airline.api.model.DTO.CreateSeatDTO;
import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import co.udea.airline.api.model.jpa.repository.seats.ISeatRepository;
import co.udea.airline.api.services.seats.service.ISeatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper implements ICustomMapper<Seat, SeatDTO> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public SeatDTO convertToDto(Seat entity) {
        return modelMapper.map(entity, SeatDTO.class);
    }

    @Override
    public Seat convertToEntity(SeatDTO entityDTO) {
        return modelMapper.map(entityDTO, Seat.class);
    }
}
