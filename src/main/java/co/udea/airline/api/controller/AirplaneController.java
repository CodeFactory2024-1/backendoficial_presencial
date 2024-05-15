package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.flightbmodel.AirplaneModel;
import co.udea.airline.api.services.flightsservices.AirplaneServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/airplanes")
@Tag(name = "Airplane Data", description = "Get information about airplane models")

public class AirplaneController {

    @Autowired
    private AirplaneServices airplaneService;

    @GetMapping("/searchairplanes")
    public List<AirplaneModel> searchAirplanes() {
        return airplaneService.searchAirplanes();
    }
}
