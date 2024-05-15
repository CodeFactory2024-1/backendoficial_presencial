package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.Booking;
import co.udea.airline.api.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/booking")
@Tag(name = "Bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/searchBookings")
    public List<Booking> getBookings() {
        return bookingService.getBookings();
    }

    @GetMapping("/{bookingId}")
    public Optional<Booking> getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @GetMapping("/{flightId}")
    public List<Booking> getBookingByFlightId(@PathVariable Long flightId) {
        return bookingService.getBookingByFlightId(flightId);
    }

    @PostMapping("/booking")
    public void saveBooking(@RequestBody Booking booking) {
        bookingService.saveOrUpdate(booking);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@PathVariable Long bookingId) {
        bookingService.delete(bookingId);
    }
}
