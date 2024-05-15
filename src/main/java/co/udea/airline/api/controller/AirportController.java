package co.udea.airline.api.controller;


import co.udea.airline.api.model.jpa.model.flightbmodel.Airport;
import co.udea.airline.api.services.flightsservices.AirportServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/airports")
@Tag(name = "Airports Data", description = "Get information about airports")

public class AirportController {
    @Autowired
    private AirportServices airportService;


    @GetMapping("/searchairports")
    public List<Airport> searchAirports() {
        return airportService.searchAirports();
    }
}
