package co.udea.airline.api.services.seats.service;

import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.DTO.SeatXPassengerDTO;
import co.udea.airline.api.model.jpa.model.bookings.Booking;
import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.jpa.model.seats.SeatXPassenger;
import co.udea.airline.api.model.jpa.repository.bookings.IBookingRepository;
import co.udea.airline.api.model.jpa.repository.bookings.IPassengerRepository;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import co.udea.airline.api.model.jpa.repository.seats.ISeatRepository;
import co.udea.airline.api.model.jpa.repository.seats.ISeatXPassengerRepository;
import co.udea.airline.api.model.mapper.SeatMapper;
import co.udea.airline.api.model.mapper.SeatXPassengerMapper;
import co.udea.airline.api.utils.common.SeatClassEnum;
import co.udea.airline.api.utils.common.SeatLocationEnum;
import co.udea.airline.api.utils.common.SeatStatusEnum;
import co.udea.airline.api.utils.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements ISeatService{

    @Autowired
    ISeatRepository seatRepository;

    @Autowired
    IFlightRepository flightRepository;

    @Autowired
    IPassengerRepository passengerRepository;

    @Autowired
    IBookingRepository bookingRepository;

    @Autowired
    ISeatXPassengerRepository seatXPassengerRepository;

    @Autowired
    SeatXPassengerMapper seatXPassengerMapper;

    @Autowired
    SeatMapper seatMapper;

    private Flight getFlightIfExists(Long id){
        Optional<Flight> flightOptional = flightRepository.findById(id);
        if (flightOptional.isEmpty()){
            throw new DataNotFoundException("Flight does not exist.");
        }
        return flightOptional.get();
    }

    private Seat getSeatIfExists(Long id){
        Optional<Seat> seatOptional = seatRepository.findById(id);
        if (seatOptional.isEmpty()){
            throw new DataNotFoundException("Seat does not exist.");
        }
        return seatOptional.get();
    }

    private Passenger getPassengerIfExists(Long id){
        Optional<Passenger> passengerOptional = passengerRepository.findById(id);
        if (passengerOptional.isEmpty()){
            throw new DataNotFoundException("Passenger does not exist");
        }
        return passengerOptional.get();
    }

    @Override
    public SeatDTO findSeatById(Long id) {
        Seat seat = getSeatIfExists(id);
        return seatMapper.convertToDto(seat);
    }

    @Override
    public List<Seat> generateSeatsByFlightId(Long id, int nSeats) {

        if(nSeats < 60){
            String msg = "Minimum number of seats is 60";
            throw new DataDuplicatedException(msg);
        } else if (nSeats > 500) {
            String msg = "This is not a intergalactic spaceship";
            throw new BusinessException(msg);
        }
        Flight flight = getFlightIfExists(id);
        if (seatRepository.existsSeatByFlightId(id)){
            String msg = "Flight already has seats";
            throw new DataDuplicatedException(msg);
        }

        Seat seat;
        String seatTag;
        List<Seat> seats = new ArrayList<>();
        float proportion;
        int rowsPerSeatClass;
        BigDecimal surcharge;

        final int COLUMNS = 6;
        final int TOTAL_ROWS = nSeats / COLUMNS; // Integer division to trunk

        // Code duplication to decrease complexity in loop and readability.
        SeatLocationEnum[] locationEnumList = new SeatLocationEnum[COLUMNS];
        locationEnumList[0] = SeatLocationEnum.WINDOW;
        locationEnumList[1] = SeatLocationEnum.CENTER;
        locationEnumList[2] = SeatLocationEnum.AISLE;
        locationEnumList[3] = SeatLocationEnum.AISLE;
        locationEnumList[4] = SeatLocationEnum.CENTER;
        locationEnumList[5] = SeatLocationEnum.WINDOW;

        String[] columnLetter = new String[COLUMNS];
        columnLetter[0] = "A";
        columnLetter[1] = "B";
        columnLetter[2] = "C";
        columnLetter[3] = "D";
        columnLetter[4] = "E";
        columnLetter[5] = "F";

        int seatPosNumber = 1; // For Tag
        for (SeatClassEnum seatClass : SeatClassEnum.values()) {
            proportion = seatClass.getProportion();
            rowsPerSeatClass = (int) (proportion*TOTAL_ROWS); // Trunks

            for (int i = 0; i < rowsPerSeatClass; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    seat = new Seat();
                    seat.setSeatNumber(seatPosNumber);
                    seat.setSeatClass(seatClass);
                    seat.setLocation(locationEnumList[j]);
                    surcharge = seat.calculateTotalSurcharge();
                    seat.setSurcharge(surcharge);
                    seat.setFlight(flight);
                    seat.setStatus(SeatStatusEnum.AVAILABLE);
                    seatTag = columnLetter[j] + "-" + (i+1);
                    seat.setTag(seatTag);
                    seats.add(seat);
                    seatPosNumber++;
                }
            }
        }
        // PERSISTING SEATS
        return seatRepository.saveAll(seats);
    }

    @Override
    public SeatXPassengerDTO assignSeatToPassenger(Long seatId, Long passengerId) {
        // Methods already handle exceptions
        Seat seat = getSeatIfExists(seatId);
        Passenger passenger = getPassengerIfExists(passengerId);
        Long flightId = passenger.getBooking().getFlight().getId();

        if (!flightId.equals(seat.getFlight().getId())){
            throw new BusinessException(
                    "Can't assign a seat that does not belong to passenger's Flight."
            );
        }
        // Check if seat is assignable, this is, it's a seat from the flight
        // of the passenger

        // Check if the exact pair seat-passenger already exists
        if (seat.getStatus().equals(SeatStatusEnum.OCCUPIED)){
            throw new DataDuplicatedException("Seat is occupied.");
        }

        if (seatXPassengerRepository.findByPassengerId(passenger.getId()).isPresent()){
            throw new DataDuplicatedException("Passenger has another seat assigned.");
        }

        //  Update seat status
        seat.setStatus(SeatStatusEnum.OCCUPIED);

        // Create SeatXPassenger entity
        SeatXPassenger seatXPassenger = new SeatXPassenger();
        seatXPassenger.setSeat(seat);
        seatXPassenger.setPassenger(passenger);

        // insert into SeatXPassenger
        SeatXPassenger savedSeatXPassenger = seatXPassengerRepository.save(seatXPassenger);
        return seatXPassengerMapper.convertToDto(savedSeatXPassenger);
    }

    @Override
    public SeatXPassengerDTO getSeatByPassengerId(Long id) {
        Passenger passenger = getPassengerIfExists(id);

        Optional<SeatXPassenger> seatXPassengerOptional = seatXPassengerRepository.findByPassengerId(id);
        if (seatXPassengerOptional.isEmpty()) {
            throw new DataNotFoundException("Passenger does not exist.");
        }
        return seatXPassengerMapper.convertToDto(seatXPassengerOptional.get());
    }

    @Transactional
    @Override
    public SeatDTO removeSeatToPassenger(Long seatId, Long passengerId) {
        Passenger passenger = getPassengerIfExists(passengerId);
        Seat seat = getSeatIfExists(seatId);
        Optional<SeatXPassenger>  seatXPassengerOptional =  seatXPassengerRepository
                .findBySeatIdAndPassengerId(seatId,passengerId);
        if (seatXPassengerOptional.isEmpty()){
            String msg = "The relation does not exists!";
            throw new DataNotFoundException(msg);
        }

        seatXPassengerRepository.deleteSeatPassenger(seatXPassengerOptional.get().getId());
        seat.setStatus(SeatStatusEnum.AVAILABLE);
        return seatMapper.convertToDto(seatRepository.save(seat));
    }

    @Override
    public SeatXPassengerDTO changeAssignedSeatToPassenger(Long newSeatId, Long passengerId) {
        Passenger passenger = getPassengerIfExists(passengerId);
        Seat newSeat = getSeatIfExists(newSeatId);

        Optional<SeatXPassenger> seatXPassengerOptional = seatXPassengerRepository.findByPassengerId(passengerId);
        if (seatXPassengerOptional.isEmpty()) {
            throw new DataNotFoundException("Passenger not found.");
        }

        if (newSeat.getStatus() == SeatStatusEnum.OCCUPIED) {
            throw new BusinessException("Seat is already occupied.");
        }
        SeatXPassenger seatXPassenger = seatXPassengerOptional.get();
        Seat oldSeat = seatXPassenger.getSeat();
        oldSeat.setStatus(SeatStatusEnum.AVAILABLE);
        seatRepository.save(oldSeat); //update old Seat Status
        newSeat.setStatus(SeatStatusEnum.OCCUPIED);
        Seat newSeatSaved = seatRepository.save(newSeat); // updating new seat status
        seatXPassenger.setSeat(newSeatSaved);
        SeatXPassenger updatedSeatXPassenger = seatXPassengerRepository.save(seatXPassenger);
        return seatXPassengerMapper.convertToDto(updatedSeatXPassenger);
    }

    private Seat selectRandomSeat(List<Seat> seats){
        int maxValIndex = 0;
        double currVal;
        double maxVal = 0d;
        for (int i = 0; i < seats.size(); i++) {
            currVal = Math.random();
            if (currVal > maxVal){
                maxVal = currVal;
                maxValIndex = i;
            }
        }
        return seats.get(maxValIndex);
    }

    @Override
    public SeatXPassengerDTO assignRandomSeatToPassenger(Long passengerId) {
        Seat seat;
        Passenger passenger = getPassengerIfExists(passengerId);
        Booking booking = bookingRepository.getReferenceById(passenger.getBooking().getId());
        Flight flight = flightRepository.getReferenceById(booking.getFlight().getId());
        List<Seat> availableSeats = seatRepository.getAllAvailableSeatByFlightId(flight.getId());

        List<Seat> touristSeats = availableSeats.stream()
                .filter(s -> s.getSeatClass() == SeatClassEnum.TOURIST)
                .collect(Collectors.toList());

        List<Seat> executiveSeats = availableSeats.stream()
                .filter(s -> s.getSeatClass() == SeatClassEnum.EXECUTIVE)
                .collect(Collectors.toList());

        List<Seat> fcSeats = availableSeats.stream()
                .filter(s -> s.getSeatClass() == SeatClassEnum.FIRST_CLASS)
                .collect(Collectors.toList());

        if (!touristSeats.isEmpty()){
            seat = selectRandomSeat(touristSeats);
        } else if (!executiveSeats.isEmpty()) {
            seat = selectRandomSeat(executiveSeats);
        } else if (!fcSeats.isEmpty()) {
            seat = selectRandomSeat(fcSeats);
        } else {
            throw new NotAvailableSeatsException("There aren't any available seats :(");
        }
        // Update seat surcharge to 0
        seat.setSurcharge(new BigDecimal("0.0"));
        Seat updatedSeat = seatRepository.save(seat);

        return this.assignSeatToPassenger(updatedSeat.getId(), passenger.getId());
    }

    @Override
    public String getTotalSurchargeByBooking(Long bookingId) {
        if(!bookingRepository.existsById(bookingId)){
            String msg = "Booking does not exist.";
            throw new DataNotFoundException(String.format(msg, bookingId));
        }

        BigDecimal totalSum = new BigDecimal("0.0");
        List<Passenger> passengerList = passengerRepository.findAllByBookingId(bookingId);

        for(Passenger p: passengerList){
            try {
                totalSum = totalSum.add(p.getSeatXPassenger().getSeat().getSurcharge());
            }
            catch (NullPointerException e) {
                String msg = "Error obtaining passenger seat. ¿Does the passenger have a seat assigned?";
                throw new BusinessException(msg);
            }
        }
        return totalSum.toString();
    }

    @Override
    public String getSeatSurcharge(Long seatId) {
        return getSeatIfExists(seatId).getSurcharge().toString();
    }

    @Override
    public SeatDTO setSeatSurcharge(Long seatId, String surcharge) {
        Seat seat = getSeatIfExists(seatId);
        seat.setSurcharge(new BigDecimal(surcharge));
        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.convertToDto(savedSeat);
    }

    @Override
    public List<Seat> getAllSeatsByFlightId(Long flightId) {
        Flight flight = getFlightIfExists(flightId);
        return seatRepository.findAllByFlightId(flightId);
    }

    private List<Passenger> getPassengerListByBookingId(Long bookingId){
        return passengerRepository.findAllByBookingId(bookingId);
    }

    @Override
    public List<SeatXPassengerDTO> getAllSeatsByBookingId(Long id) {
        if(!bookingRepository.existsById(id)){
            throw new DataNotFoundException("Booking does not exist.");
        }
        Optional<SeatXPassenger> passengerOptional;
        List<SeatXPassengerDTO> spList = new ArrayList<>();
        List<Passenger> passengerList = getPassengerListByBookingId(id);
        for(Passenger p: passengerList){
            passengerOptional = seatXPassengerRepository.findByPassengerId(p.getId());
            if (passengerOptional.isPresent()){
                spList.add(seatXPassengerMapper.convertToDto(passengerOptional.get()));
            } else {
                SeatXPassenger spWithNoSeat = new SeatXPassenger();
                spWithNoSeat.setPassenger(p);
                spWithNoSeat.setSeat(null);
                spList.add(seatXPassengerMapper.convertToDto(spWithNoSeat));
            }
        }
        return spList;
    }
}
