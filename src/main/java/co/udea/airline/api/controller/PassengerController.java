package co.udea.airline.api.controller;
import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.services.bookingservices.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/passenger")
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
}
