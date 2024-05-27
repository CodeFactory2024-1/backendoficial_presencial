package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.service.PassengerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/passengers")
    public List<Passenger> getPassengers() {
        return passengerService.getPassengers();
    }

    @GetMapping("/{passengerId}")
    public Optional<Passenger> getPassengerById(@PathVariable Long passengerId) {
        return passengerService.getPassenger(passengerId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/passenger")
    @SecurityRequirement(name = "JWT")
    public void savePassenger(@RequestBody Passenger passenger) {
        passengerService.saveOrUpdate(passenger);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{passengerId}")
    @SecurityRequirement(name = "JWT")
    public void deletePassenger(@PathVariable Long passengerId){
        passengerService.delete(passengerId);
    }

    @GetMapping("/booking/{bookingId}")
    public List<Passenger> getPassengerByBookingId(@PathVariable Long bookingId) {
        return passengerService.findByBookingId(bookingId);
    }

    @GetMapping("/person/{personId}")
    public List<Passenger> getPassengerByPersonId(@PathVariable Long personId) {
        return passengerService.findByPersonId(personId);
    }
}
