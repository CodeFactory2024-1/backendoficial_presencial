package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/passenger")
@CrossOrigin
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

    @PostMapping("/passenger")
    public void savePassenger(@RequestBody Passenger passenger) {
        passengerService.saveOrUpdate(passenger);
    }

    @DeleteMapping("/{passengerId}")
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
