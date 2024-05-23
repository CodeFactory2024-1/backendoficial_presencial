package co.udea.airline.api.services.seats.service;

import co.udea.airline.api.model.DTO.CreateSeatDTO;
import co.udea.airline.api.model.DTO.SeatXPassengerDTO;
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
import co.udea.airline.api.model.mapper.SeatXPassengerMapper;
import co.udea.airline.api.utils.common.Messages;
import co.udea.airline.api.utils.common.SeatClassEnum;
import co.udea.airline.api.utils.common.SeatLocationEnum;
import co.udea.airline.api.utils.common.SeatStatusEnum;
import co.udea.airline.api.utils.exception.DataDuplicatedException;
import co.udea.airline.api.utils.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * SEATS SETUP DEPENDS ON AICRAFT
     * We have three Seat class type: TOURIST, EXECUTIVE AND FIRST_CLASS.
     * Seats distribution goes like this: 80% tourist, 10% executive,
     * and 10% to first class. If we need to change those proportions or if you
     * need to have a different number of seats
     * service must not be used.
     * @param id Flight id
     * @return List<Seat> List of seats.
     */
    @Override
    public List<Seat> generateSeatsByFlightId(Long id) {
        // Seat Config preparation
        // Given K columns, the proportion that meet
        // business requirements is the following
        // first_class:executive:tourist := K:K:8K
        // We only need 1 row for first_class
        // 1 row for executive
        // 8 rows for tourist
        // total rows = 10

        Flight flight = getFlightIfExists(id);
        if (seatRepository.existsSeatByFlightId(id)){
            String msg = String.format(messages.get("flight.has.seats"));
            throw new DataDuplicatedException(msg);
        }

        final int COLUMNS = 6;
        final int ROWS = 10;
        final int TOTAL_SEATS = ROWS*COLUMNS;

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

        String seatTag;
        String seatCodename;
        SeatClassEnum seatClass;
        List<Seat> seats = new ArrayList<>();
        int seatPosNumber = 1; // For Tag

        // GENERATING SEATS
        // Rows loop
        for (int i = 0; i < ROWS; i++) {
            // First rows is First_class. Changing Airplane structure
            // changes this
            int i_aux = i+1;
            if (i_aux == 1){
                seatClass = SeatClassEnum.FIRST_CLASS;
            }
            // The next rows are Executive class
            else if (i_aux == 2) {
                seatClass = SeatClassEnum.EXECUTIVE;
            }
            // last rows are Tourist class type
            else  {
                seatClass = SeatClassEnum.TOURIST;
            }
            // Columns loop
            for (int j = 0; j < COLUMNS; j++) {
                // Preparing new Seat
                Seat seat = new Seat();
                seat.setStatus(SeatStatusEnum.AVAILABLE);
                seat.setLocation(locationEnumList[j]);
                seat.setSeatClass(seatClass);
                seat.setFlight(flight);
                seatTag = columnLetter[j] + "-" + Integer.toString(i_aux);
                seat.setTag(seatTag);
                seatCodename = Long.toString(flight.getId()) +
                        "-" +
                        Integer.toString(seatPosNumber) +
                        "-" +
                        seatClass.getTag();
                seat.setCodename(seatCodename);

                // Adding to List of seat
                seats.add(seat);
                seatPosNumber++;
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
        return null;
    }

    @Override
    public List<Seat> getAllSeatsByFlightId(Long flightId) {
        Flight flight = getFlightIfExists(flightId);
        return seatRepository.findAllByFlightId(flightId);
    }
}
