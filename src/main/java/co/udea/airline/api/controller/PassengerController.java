package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.Booking;
import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.service.BookingService;
import co.udea.airline.api.service.PassengerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/passenger")
@CrossOrigin
@Tag(name = "Passenger")
public class PassengerController {
    @Autowired
    private PassengerService passengerService;
    private BookingService bookingService;

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/passengers")
    public List<Passenger> getPassengers(@AuthenticationPrincipal Jwt jwt) {
        return passengerService.getPassengers();
    }

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/{passengerId}")
    public Optional<Passenger> getPassengerById(@AuthenticationPrincipal Jwt jwt,@PathVariable Long passengerId) {
        return passengerService.getPassenger(passengerId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/passenger")
    @SecurityRequirement(name = "JWT")
    public void savePassenger(@RequestBody Passenger passenger) {
        passengerService.saveOrUpdate(passenger);
    }

   /*
   @PreAuthorize("hasRole('ADMIN')")
   @DeleteMapping("/{passengerId}")
   @SecurityRequirement(name = "JWT")
    public void deletePassenger(@PathVariable Long passengerId){
        passengerService.delete(passengerId);
    }*/

    /*@PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/booking/{bookingId}")
    public List<Passenger> getPassengerByBookingId(@AuthenticationPrincipal Jwt jwt,@PathVariable Long bookingId) {
        return bookingService.findById(bookingId);
    }*/
    /*@PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/person/{personId}")
    public List<Passenger> getPassengerByPersonId(@AuthenticationPrincipal Jwt jwt,@PathVariable Long personId) {
        return passengerService.findById(personId);}
    */
}
