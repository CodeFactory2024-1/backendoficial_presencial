package co.udea.airline.api;


import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
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
    CommandLineRunner runner(IFlightRepository flightRepository, ISeatRepository seatRepository) {
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
            seat.setTag("Test-1");
            seat.setCodename("FlightID-SeatNumber-ClassTag");
            seatRepository.save(seat);

        };
    }

}
