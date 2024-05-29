package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.service.PassengerService;
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
    @PreAuthorize("hasAuthority('save:booking')")
    @PostMapping("/passenger")
    public void savePassenger(@AuthenticationPrincipal Jwt jwt,@RequestBody Passenger passenger) {
        passengerService.saveOrUpdate(passenger);
    }

   /*
   @PreAuthorize("hasAuthority('save:booking')")
   @DeleteMapping("/{passengerId}")
    public void deletePassenger(@AuthenticationPrincipal Jwt jwt,@PathVariable Long passengerId){
        passengerService.delete(passengerId);
    }*/

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/booking/{bookingId}")
    public List<Passenger> getPassengerByBookingId(@AuthenticationPrincipal Jwt jwt,@PathVariable Long bookingId) {
        return passengerService.findByBookingId(bookingId);
    }
    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/person/{personId}")
    public List<Passenger> getPassengerByPersonId(@AuthenticationPrincipal Jwt jwt,@PathVariable Long personId) {
        return passengerService.findByPersonId(personId);
    }
}
