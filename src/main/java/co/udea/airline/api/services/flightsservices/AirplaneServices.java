package co.udea.airline.api.services.flightsservices;

import co.udea.airline.api.model.jpa.model.flightbmodel.AirplaneModel;
import co.udea.airline.api.model.jpa.repository.flightbrepository.IAirplaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AirplaneServices {
    @Autowired
    private IAirplaneRepository airplaneRepository;


    public List<AirplaneModel> searchAirplanes() {
        return airplaneRepository.findAll();
    }
}
