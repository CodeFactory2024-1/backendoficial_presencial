package co.udea.airline.api.services.seats.service;

import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.DTO.SeatXPassengerDTO;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import java.util.List;

public interface ISeatService {
    SeatDTO findSeatById(Long id);
    List<Seat> generateSeatsByFlightId(Long id, int nSeats);
    List<Seat> getAllSeatsByFlightId(Long id);
    SeatXPassengerDTO getSeatByPassengerId(Long id);
    SeatXPassengerDTO assignSeatToPassenger(Long seatId, Long passengerId);
    SeatDTO removeSeatToPassenger(Long seatId, Long passengerId);
    SeatXPassengerDTO changeAssignedSeatToPassenger(Long newSeatId, Long passengerId);
    SeatXPassengerDTO assignRandomSeatToPassenger(Long passengerId);
    String getTotalSurchargeByBooking(Long bookingId);
    String getSeatSurcharge(Long seatId);
    SeatDTO setSeatSurcharge(Long seatId, String surcharge);
    List<SeatXPassengerDTO> getAllSeatsByBookingId(Long id);
}
