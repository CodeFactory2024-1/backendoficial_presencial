package co.udea.airline.api.service;

import co.udea.airline.api.model.jpa.model.Flight;
import co.udea.airline.api.model.jpa.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    @Autowired
    FlightRepository flightRepository;

    public List<Flight> getFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlight(Long id) {
        return flightRepository.findById(id);
    }

    public void saveOrUpdate(Flight booking) {
        flightRepository.save(booking);
    }

    /*public void delete(Long id) {
        flightRepository.deleteById(id);
    }*/
}
