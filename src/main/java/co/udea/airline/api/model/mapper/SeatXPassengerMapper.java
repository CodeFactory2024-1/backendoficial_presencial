package co.udea.airline.api.model.mapper;

import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.DTO.SeatXPassengerDTO;
import co.udea.airline.api.model.jpa.model.seats.SeatXPassenger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class SeatXPassengerMapper implements ICustomMapper<SeatXPassenger, SeatXPassengerDTO> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public SeatXPassengerDTO convertToDto(SeatXPassenger entity) {
        return modelMapper.map(entity, SeatXPassengerDTO.class);
    }

    @Override
    public SeatXPassenger convertToEntity(SeatXPassengerDTO entityDTO) {
        return modelMapper.map(entityDTO, SeatXPassenger.class);
    }
}
