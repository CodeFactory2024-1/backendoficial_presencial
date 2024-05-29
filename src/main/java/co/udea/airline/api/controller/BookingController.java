package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.Booking;
import co.udea.airline.api.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/booking")
@CrossOrigin
@Tag(name = "Bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/bookings")
    public List<Booking> getBookings(@AuthenticationPrincipal Jwt jwt) {
        return bookingService.getBookings();
    }

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/{bookingId}")
    public Optional<Booking> getBookingById(@AuthenticationPrincipal Jwt jwt,@PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/searchBookingByFlightId/{flightId}")
    public List<Booking> getBookingByFlightId(@AuthenticationPrincipal Jwt jwt,@PathVariable Long flightId) {
        return bookingService.getBookingByFlightId(flightId);
    }
    @PreAuthorize("hasAuthority('save:booking')")
    @PostMapping("/booking")
    public void saveBooking(@AuthenticationPrincipal Jwt jwt, @RequestBody Booking booking) {
        bookingService.saveOrUpdate(booking);
    }
    @PreAuthorize("hasAuthority('save:booking')")
    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@AuthenticationPrincipal Jwt jwt,@PathVariable Long bookingId){
        bookingService.delete(bookingId);
    }
}
