package co.udea.airline.api.services.flightsservices;


import co.udea.airline.api.model.jpa.model.flightbmodel.AirplaneModel;
import co.udea.airline.api.model.jpa.model.flightbmodel.Airport;
import co.udea.airline.api.model.jpa.model.flightbmodel.Scale;
import co.udea.airline.api.model.jpa.repository.flightbrepository.IAirplaneRepository;
import co.udea.airline.api.model.jpa.repository.flightbrepository.IAirportRespository;
import co.udea.airline.api.model.jpa.repository.flightbrepository.IScaleRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScaleServices {

    @Autowired
    private IScaleRespository scaleRepository;

    @Autowired
    private IAirplaneRepository airplaneRepository;

    @Autowired
    private IAirportRespository airportRespository;

    /**
     * Saves a scale after validating the origin and destination airports, departure and arrival times.
     *
     * @param scale The scale to be saved.
     * @return The saved scale.
     */
    public Scale saveScale(Scale scale) {
        validateOriginAndDestination(scale);
        validateTimes(scale);

        Airport originAirport = airportRespository.findById(scale.getOriginAirport().getId()).orElse(null);
        Airport destinationAirport = airportRespository.findById(scale.getDestinationAirport().getId()).orElse(null);
        AirplaneModel airplaneModel = airplaneRepository.findById(scale.getAirplaneModel().getId()).orElse(null);

        if (originAirport == null || destinationAirport == null || airplaneModel == null) {
            throw new IllegalArgumentException("Debe ingresar los campos de origen, destino y modelo de avión.");
        }
        scale.setOriginAirport(originAirport);
        scale.setDestinationAirport(destinationAirport);
        scale.setAirplaneModel(airplaneModel);

        return scaleRepository.save(scale);
    }

    private void validateOriginAndDestination(Scale scale) {
        if (scale.getOriginAirport().equals(scale.getDestinationAirport())) {
            throw new IllegalArgumentException("La ciudad de origen y destino no pueden ser la misma.");
        }
    }

    public Scale updateScale(Scale scale) {
        Scale updatedScale = scaleRepository.findById(scale.getId()).orElse(null);

        if (updatedScale == null) {
            return saveScale(scale);
        }

        Airport originRes = airportRespository.findByIATA(scale.getOriginAirport().getId());
        Airport destinationRes = airportRespository.findByIATA(scale.getDestinationAirport().getId());
        AirplaneModel airplaneModelRes = airplaneRepository.findById(scale.getAirplaneModel().getId()).get();

        updatedScale.setOriginAirport(originRes);
        updatedScale.setDestinationAirport(destinationRes);
        updatedScale.setAirplaneModel(airplaneModelRes);
        updatedScale.setArrivalDate(scale.getArrivalDate());
        updatedScale.setDepartureDate(scale.getDepartureDate());

        scaleRepository.save(updatedScale);
        return updatedScale;
    }

    public void deleteScaleById(Long id) {
        scaleRepository.deleteById(id);
    }

    private void validateTimes(Scale scale) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureTime = scale.getDepartureDate();
        LocalDateTime arrivalTime = scale.getArrivalDate();

        if (departureTime == null || arrivalTime == null) {
            throw new IllegalArgumentException("Las horas de salida y llegada son obligatorias.");
        }

        if (departureTime.isBefore(now.plusHours(1))) {
            throw new IllegalArgumentException("La hora de salida debe ser al menos una hora después de la hora actual.");
        }

        if (arrivalTime.isBefore(departureTime)) {
            throw new IllegalArgumentException("La hora de llegada debe ser posterior a la hora de salida.");
        }
    }
}
