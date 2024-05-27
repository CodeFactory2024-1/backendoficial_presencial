package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.service.PassengerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/passenger")
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

    @PostMapping("/passenger")
    public Long savePassenger(@RequestBody Passenger passenger) {
        passengerService.saveOrUpdate(passenger);
        return passenger.getPassengerId();
    }
}
