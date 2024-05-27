package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.Flight;
import co.udea.airline.api.service.FlightService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/flight")
@Tag(name = "Flights")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    public List<Flight> getFlights() {
        return flightService.getFlights();
    }

}
