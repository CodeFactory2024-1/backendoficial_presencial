package co.udea.airline.api.controller;


import co.udea.airline.api.model.jpa.model.Booking;
import co.udea.airline.api.model.jpa.model.Booking_Passenger;
import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.service.BookingPassengerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/bookingPassenger")
@CrossOrigin
@Tag(name = "Booking Passenger")
public class BookingPassengerController {

    @Autowired
    private BookingPassengerService bookingPassengerService;

    @GetMapping("/bookingsPassengers")
    public List<Booking_Passenger> getBookingsPassengers() {
        return bookingPassengerService.getBookingsPassengers();
    }

    @GetMapping("/{booking_passenger_id}")
    public Optional<Booking_Passenger> getBookingPassengerById(@PathVariable Long booking_passenger_id) {
        return bookingPassengerService.getBookingPassenger(booking_passenger_id);
    }
    @PreAuthorize("hasAuthority('save:booking')")
    @PostMapping("/bookingPassenger")
    public void saveFlight(@AuthenticationPrincipal Jwt jwt, @RequestBody Booking_Passenger bookingPassenger) {
        bookingPassengerService.saveOrUpdate(bookingPassenger);
    }

    @GetMapping("/findBookingsByPassenger/{passengerId}")
    public List<Booking> getBookingPassengersByPassengerId(@PathVariable Long passengerId) {
        return bookingPassengerService.getBookingPassengersByPassengerId(passengerId);
    }

    @GetMapping("/findPassengersByBooking/{bookingId}")
    public List<Passenger> getBookingPassengersByBookingId(@PathVariable Long bookingId) {
        return bookingPassengerService.getBookingPassengersByBookingId(bookingId);
    }

}
