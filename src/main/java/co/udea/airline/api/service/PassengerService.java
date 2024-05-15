package co.udea.airline.api.service;

import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.model.jpa.repository.PassengerRepository;
import co.udea.airline.api.utils.exception.DataDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {
    @Autowired
    PassengerRepository passengerRepository;

    public List<Passenger> getPassengers() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> getPassenger(Long id) {
        return passengerRepository.findById(id);
    }

    public void saveOrUpdate(Passenger passenger) {

        Long passengerExistence = passenger.getPersonId();
        if (passengerExistence > 0) {
            Optional<Passenger> passengerFound = passengerRepository.findById(passengerExistence);
            if (passengerFound.isPresent()) {
                throw new DataDuplicatedException("The Person Id has already exists");
            }
            ;
        }

        passengerRepository.save(passenger);
    }

    public void delete(Long id) {
        passengerRepository.deleteById(id);
    }
}
