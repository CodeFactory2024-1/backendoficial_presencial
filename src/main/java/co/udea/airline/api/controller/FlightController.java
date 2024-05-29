package co.udea.airline.api.controller;


import co.udea.airline.api.model.jpa.model.Flight;
import co.udea.airline.api.service.FlightService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/flight")
@CrossOrigin
@Tag(name = "Flights")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/flights")
    public List<Flight> getFlights(@AuthenticationPrincipal Jwt jwt) {
        return flightService.getFlights();
    }

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/{flightId}")
    public Optional<Flight> getFlightById(@AuthenticationPrincipal Jwt jwt,@PathVariable Long flightId) {
        return flightService.getFlight(flightId);
    }
    @PreAuthorize("hasAuthority('save:booking')")
    @PostMapping("/flight")
    public void saveFlight(@AuthenticationPrincipal Jwt jwt, @RequestBody Flight flight) {
        flightService.saveOrUpdate(flight);
    }

    /*
    @PreAuthorize("hasAuthority('save:booking')")
    @DeleteMapping("/{flightId}")
    public void deleteFlight(@AuthenticationPrincipal Jwt jwt,@PathVariable Long flightId){
        flightService.delete(flightId);
    }*/
}
