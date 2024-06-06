package co.udea.airline.api.services.flights.service;

import co.udea.airline.api.model.jpa.model.flights.AirplaneModel;
import co.udea.airline.api.model.jpa.model.flights.Airport;
import co.udea.airline.api.model.jpa.model.flights.Scale;
import co.udea.airline.api.model.jpa.repository.flights.IAirplaneModelRepository;
import co.udea.airline.api.model.jpa.repository.flights.IAirportRepository;
import co.udea.airline.api.model.jpa.repository.flights.IScaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class represents a service for managing scales in an airline system.
 * It provides methods for saving, deleting, and updating scales.
 */
@Service
public class ScaleService {
    @Autowired
    private IScaleRepository scaleRepository;

    @Autowired
    private IAirplaneModelRepository airplaneModelRepository;

    @Autowired
    private IAirportRepository airportRepository;

    public Scale saveScale(Scale scale) {
        Airport originRes = airportRepository.findByIATA(scale.getOriginAirport().getId());
        Airport destinationRes = airportRepository.findByIATA(scale.getDestinationAirport().getId());
        AirplaneModel airplaneModelRes = airplaneModelRepository.findById(scale.getAirplaneModel().getId()).get();

        // TODO: Verify if the entities are not null

        scale.setOriginAirport(originRes);
        scale.setDestinationAirport(destinationRes);
        scale.setAirplaneModel(airplaneModelRes);
        scaleRepository.save(scale);

        return scale;
    }

    public void deleteScaleById(Long id) {
        scaleRepository.deleteById(id);
    }

    /**
     * Represents a scale in a flight itinerary.
     */
    public Scale updateScale(Scale scale) {
        Scale updatedScale = scaleRepository.findById(scale.getId()).orElse(null);

        if (updatedScale == null) {
            return saveScale(scale);
        }

        Airport originRes = airportRepository.findByIATA(scale.getOriginAirport().getId());
        Airport destinationRes = airportRepository.findByIATA(scale.getDestinationAirport().getId());
        AirplaneModel airplaneModelRes = airplaneModelRepository.findById(scale.getAirplaneModel().getId()).get();

        updatedScale.setOriginAirport(originRes);
        updatedScale.setDestinationAirport(destinationRes);
        updatedScale.setAirplaneModel(airplaneModelRes);
        updatedScale.setArrivalDate(scale.getArrivalDate());
        updatedScale.setDepartureDate(scale.getDepartureDate());

        scaleRepository.save(updatedScale);
        return updatedScale;
    }
}
