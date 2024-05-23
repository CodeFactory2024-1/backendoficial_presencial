package co.udea.airline.api;


import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.model.jpa.model.seats.SeatXPassenger;
import co.udea.airline.api.model.jpa.repository.bookings.IPassengerRepository;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import co.udea.airline.api.model.jpa.repository.seats.ISeatXPassengerRepository;
import co.udea.airline.api.utils.common.FlightTypeEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// Testing
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import co.udea.airline.api.model.jpa.model.seats.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import co.udea.airline.api.model.jpa.repository.seats.ISeatRepository;
import co.udea.airline.api.utils.common.SeatClassEnum;
import co.udea.airline.api.utils.common.SeatLocationEnum;



//@EnableTransactionManagement
@SpringBootApplication
@ComponentScan({
        "co.udea.airline.api.controller",
        "co.udea.airline.api.model",
        "co.udea.airline.api.services",
        "co.udea.airline.api.utils",
})

public class AirlineApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AirlineApiApplication.class, args);
    }

    // Bean for Testing Seats
//    @Bean
//    CommandLineRunner runner(ISeatRepository seatRepository) {
//        return args -> {
//
//            Seat seat = new Seat();
//            seat.setSeatClass(SeatClassEnum.FIRST_CLASS);
//            seat.setLocation(SeatLocationEnum.AISLE);
//
//            seatRepository.save(seat);
//            Seat saved = seatRepository.findById(seat.getId()).orElseThrow(NoSuchElementException::new);
//        };
//    }

     // Testing Flights
    @Bean
    CommandLineRunner runner(IFlightRepository flightRepository,
                             ISeatRepository seatRepository,
                             IPassengerRepository passengerRepository,
                             ISeatXPassengerRepository seatXPassengerRepository) {
        return args -> {

            Flight flight1 = new Flight();
            flight1.setFlightType(FlightTypeEnum.International);

            Flight flight2 = new Flight();
            flight2.setFlightType(FlightTypeEnum.Domestic);

            List<Flight> flightArrayList = new ArrayList<>();
            flightArrayList.add(flight1);
            flightArrayList.add(flight2);

            List<Flight> savedFlights = flightRepository.saveAll(flightArrayList);

            // Generating Seat
            Seat seat = new Seat();
            seat.setSeatClass(SeatClassEnum.FIRST_CLASS);
            seat.setLocation(SeatLocationEnum.AISLE);
            seat.setFlight(savedFlights.get(1));
            seat.setSeatNumber(25);
            seat.setTag("Test-1");
            seatRepository.save(seat);

            // Generating a Passenger
            Passenger passenger = new Passenger();
            passenger.setName("Gomecito");
            passengerRepository.save(passenger);

            SeatXPassenger seatXPassenger = new SeatXPassenger();
            seatXPassenger.setPassenger(passenger);
            seatXPassenger.setSeat(seat);
            seatXPassengerRepository.save(seatXPassenger);

            // Otros datos de prueba...
            Seat seat2 = new Seat();
            seat2.setSeatClass(SeatClassEnum.EXECUTIVE);
            seat2.setLocation(SeatLocationEnum.CENTER);
            seat2.setFlight(savedFlights.get(1));
            seat2.setSeatNumber(13);
            seat2.setTag("Test-2");
            seatRepository.save(seat2);

            // Generating a Passenger
            Passenger passenger2 = new Passenger();
            passenger2.setName("Daniel");
            passengerRepository.save(passenger2);

            SeatXPassenger seatXPassenger2 = new SeatXPassenger();
            seatXPassenger2.setPassenger(passenger2);
            seatXPassenger2.setSeat(seat2);
            seatXPassengerRepository.save(seatXPassenger2);
        };
    }

}
