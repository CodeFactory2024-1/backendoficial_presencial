package co.udea.airline.api.services.seats.service;

import co.udea.airline.api.model.DTO.CreateSeatDTO;
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
import co.udea.airline.api.model.mapper.CreateSeatMapper;
import co.udea.airline.api.model.mapper.SeatMapper;
import co.udea.airline.api.model.mapper.SeatXPassengerMapper;
import co.udea.airline.api.utils.common.Messages;
import co.udea.airline.api.utils.common.SeatClassEnum;
import co.udea.airline.api.utils.common.SeatLocationEnum;
import co.udea.airline.api.utils.common.SeatStatusEnum;
import co.udea.airline.api.utils.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private IFlightRepository flightRepository;

    @Autowired
    private IPassengerRepository passengerRepository;

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private ISeatXPassengerRepository seatXPassengerRepository;

    @Autowired
    private Messages messages;

    @Autowired
    private CreateSeatMapper createSeatMapper;

    @Autowired
    private SeatXPassengerMapper seatXPassengerMapper;

    @Autowired
    private SeatMapper seatMapper;

    private Flight getFlightIfExists(Long id){
        Optional<Flight> flightOptional = flightRepository.findById(id);
        if (flightOptional.isEmpty()){
            throw new DataNotFoundException(String.format(messages.get("flight.does.not.exist")));
        }
        return flightOptional.get();
    }

    private Seat getSeatIfExists(Long id){
        Optional<Seat> seatOptional = seatRepository.findById(id);
        if (seatOptional.isEmpty()){
            throw new DataNotFoundException(String.format(messages.get("seat.does.not.exist")));
        }
        return seatOptional.get();
    }

    private Passenger getPassengerIfExists(Long id){
        Optional<Passenger> passengerOptional = passengerRepository.findById(id);
        if (passengerOptional.isEmpty()){
            throw new DataNotFoundException(String.format(messages.get("passenger.does.not.exist")));
        }
        return passengerOptional.get();
    }

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
        return createSeatMapper.convertToDto(savedSeat);
    }

//    public Seat update(Seat seat) {
//        Optional<Seat> seatOptional = seatRepository.findById(seat.getId());
//        if (seatOptional.isEmpty()){
//            throw new DataNotFoundException(String.format(messages.get("seat.does.not.exist")));
//        }
//        return seatRepository.save(seat);
//    }

    @Override
    public Optional<Seat> findSeatById(Long id) {
        return seatRepository.findById(id);
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
            String msg = String.format(messages.get("flight.has.seats"));
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

        // Check if the exact pair seat-passenger already exists
        if (seatXPassengerRepository.existsBySeatIdAndPassengerId(
                seatId, passengerId)){
            throw new DataDuplicatedException(String.format(
                    messages.get("seat.passenger.already.exists")));
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
            throw new DataNotFoundException(String.format(messages.get("seat.passenger.not.found")));
        }
        return seatXPassengerMapper.convertToDto(seatXPassengerOptional.get());
    }

    @Override
    public SeatXPassengerDTO removeSeatToPassenger(Long seatId, Long passengerId) {
        return null;
    }

    @Override
    public SeatXPassengerDTO updateSeatToPassenger(Long newSeatId, Long passengerId) {
        Passenger passenger = getPassengerIfExists(passengerId);
        Seat newSeat = getSeatIfExists(newSeatId);

        Optional<SeatXPassenger> seatXPassengerOptional = seatXPassengerRepository.findByPassengerId(passengerId);
        if (seatXPassengerOptional.isEmpty()) {
            throw new DataNotFoundException(String.format(messages.get("seat.passenger.not.found")));
        }

        if (newSeat.getStatus() == SeatStatusEnum.OCCUPIED) {
            throw new BusinessException(String.format(messages.get("seat.is.occupied")));
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
        Booking booking = bookingRepository.getReferenceById(passenger.getId());
        Flight flight = flightRepository.getReferenceById(booking.getId());
        List<Seat> availableSeats = seatRepository.getAllAvailableStatus(flight.getId());

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
    public List<Seat> getAllSeatsByFlightId(Long flightId) {
        Flight flight = getFlightIfExists(flightId);
        return seatRepository.findAllByFlightId(flightId);
    }
}
