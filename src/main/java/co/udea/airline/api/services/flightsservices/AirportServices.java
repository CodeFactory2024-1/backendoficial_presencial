package co.udea.airline.api.services.flightsservices;

import co.udea.airline.api.model.jpa.model.flightbmodel.Airport;
import co.udea.airline.api.model.jpa.repository.flightbrepository.IAirportRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AirportServices {

    @Autowired
    private IAirportRespository airportRespository;


    public List<Airport> searchAirports() {
        return airportRespository.findAll();
    }
}
