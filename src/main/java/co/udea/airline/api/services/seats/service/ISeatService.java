package co.udea.airline.api.services.seats.service;

import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.DTO.SeatXPassengerDTO;
import co.udea.airline.api.model.jpa.model.seats.Seat;

import java.util.List;
import java.util.Optional;

public interface ISeatService {
    //Seat save(Seat s);
    //Seat update(Seat seat);
    Optional<Seat> findSeatById(Long id);
    List<Seat> generateSeatsByFlightId(Long id, int nSeats);
    List<Seat> getAllSeatsByFlightId(Long id);
    SeatXPassengerDTO getSeatByPassengerId(Long id);
    SeatXPassengerDTO assignSeatToPassenger(Long seatId, Long passengerId);
    SeatXPassengerDTO removeSeatToPassenger(Long seatId, Long passengerId);
    SeatXPassengerDTO updateSeatToPassenger(Long newSeatId, Long passengerId);
    SeatDTO assignRandomSeatToPassenger(Long passengerId);

}
