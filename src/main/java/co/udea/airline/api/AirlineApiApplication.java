package co.udea.airline.api;


import co.udea.airline.api.model.jpa.model.bookings.Booking;
import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.model.jpa.model.seats.SeatXPassenger;
import co.udea.airline.api.model.jpa.repository.bookings.IBookingRepository;
import co.udea.airline.api.model.jpa.repository.bookings.IPassengerRepository;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import co.udea.airline.api.model.jpa.repository.seats.ISeatXPassengerRepository;
import co.udea.airline.api.utils.common.FlightTypeEnum;
import co.udea.airline.api.utils.common.SeatStatusEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

// Testing
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import co.udea.airline.api.model.jpa.model.seats.Seat;

import java.awt.print.Book;
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

     // Testing
    @Bean
    CommandLineRunner runner(IFlightRepository flightRepository,
                             ISeatRepository seatRepository,
                             IPassengerRepository passengerRepository,
                             ISeatXPassengerRepository seatXPassengerRepository,
                             IBookingRepository bookingRepository) {
        return args -> {

            Flight flight1 = new Flight();
            flight1.setFlightType(FlightTypeEnum.International);

            Flight flight2 = new Flight();
            flight2.setFlightType(FlightTypeEnum.Domestic);

            List<Flight> flightArrayList = new ArrayList<>();
            flightArrayList.add(flight1);
            flightArrayList.add(flight2);
            List<Flight> savedFlights = flightRepository.saveAll(flightArrayList);
            flightRepository.flush();

            // Generating some Bookings
            Booking booking1 = new Booking();
            booking1.setCodename("TestBooking-1");
            booking1.setFlight(savedFlights.get(0));

            Booking booking2 = new Booking();
            booking2.setCodename("TestBooking-2");
            booking2.setFlight(savedFlights.get(0));

            Booking booking3 = new Booking();
            booking3.setCodename("TestBooking-3");
            booking3.setFlight(savedFlights.get(1));

            List<Booking> bookingArrayList = new ArrayList<>();
            bookingArrayList.add(booking1);
            bookingArrayList.add(booking2);
            bookingArrayList.add(booking3);
            List<Booking> savedBookings = bookingRepository.saveAll(bookingArrayList);
            bookingRepository.flush();

            // Generating a Passenger
            Passenger passenger1 = new Passenger();
            passenger1.setName("JohnDoe");
            passenger1.setBooking(savedBookings.get(0));

            Passenger passenger2 = new Passenger();
            passenger2.setName("Juan");
            passenger2.setBooking(savedBookings.get(1));

            Passenger passenger3 = new Passenger();
            passenger3.setName("Sara");
            passenger3.setBooking(savedBookings.get(1));

            Passenger passenger4 = new Passenger();
            passenger4.setName("Sofia");
            passenger4.setBooking(savedBookings.get(2));

            List<Passenger> passengerArrayList = new ArrayList<>();
            passengerArrayList.add(passenger1);
            passengerArrayList.add(passenger2);
            passengerArrayList.add(passenger3);
            passengerArrayList.add(passenger4);
            List<Passenger> savedPassengers = passengerRepository.saveAll(passengerArrayList);
            passengerRepository.flush();

        };
    }

}
