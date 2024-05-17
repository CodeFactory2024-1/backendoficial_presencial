package co.udea.airline.api.services.seats.service;

import co.udea.airline.api.model.DTO.CreateSeatDTO;
import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.jpa.model.vehicles.Aircraft;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import co.udea.airline.api.model.jpa.repository.seats.ISeatRepository;
import co.udea.airline.api.model.mapper.CreateSeatMapper;
import co.udea.airline.api.utils.common.Messages;
import co.udea.airline.api.utils.exception.BusinessException;
import co.udea.airline.api.utils.exception.DataDuplicatedException;
import co.udea.airline.api.utils.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class SeatServiceImpl implements ISeatService{

    @Autowired
    ISeatRepository seatRepository;

    @Autowired
    IFlightRepository flightRepository;

    @Autowired
    Messages messages;

    @Autowired
    private CreateSeatMapper createSeatMapper;

    public CreateSeatDTO save(CreateSeatDTO seatToSave){
        Long flightId = seatToSave.getFlightId();
        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isEmpty()){
            throw new DataNotFoundException(String.format(messages.get("flight.does.not.exist")));
        }

        // To-Do: check if seat already exists.

        // Preparing to save
        Seat seat = createSeatMapper.convertToEntity(seatToSave);
        seat.setFlight(flightOptional.get());

        Seat savedSeat = seatRepository.save(seat);

        // Prepare the response
        CreateSeatDTO seatResponseDto = createSeatMapper.convertToDto(savedSeat);
        return seatResponseDto;
    }

    public Seat update(Seat seat) {
        Optional<Seat> seatOptional = seatRepository.findById(seat.getId());
        if (seatOptional.isEmpty()){
            throw new DataNotFoundException(String.format(messages.get("seat.does.not.exist")));
        }
        return seatRepository.save(seat);
    }

    @Override
    public Optional<Seat> findSeatById(Long id) {
        return seatRepository.findById(id);
    }
}
