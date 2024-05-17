package co.udea.airline.api.model.mapper;

import co.udea.airline.api.model.DTO.CreateSeatDTO;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateSeatMapper implements ICustomMapper<Seat, CreateSeatDTO> {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IFlightRepository flightRepository;

    @Override
    public CreateSeatDTO convertToDto(Seat entity) {
        return modelMapper.map(entity, CreateSeatDTO.class);
    }

    @Override
    public Seat convertToEntity(CreateSeatDTO entityDTO) {
        return modelMapper.map(entityDTO, Seat.class);
    }

    @Autowired
    public void addMapping() {
        modelMapper.typeMap(CreateSeatDTO.class, Seat.class).addMappings(mapper -> mapper.skip(Seat::setId));
        //modelMapper.typeMap(CreateSeatDTO.class, Seat.class).addMappings(mapper -> mapper.skip(Seat::setFlight));

    }
}
