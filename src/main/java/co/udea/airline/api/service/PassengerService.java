package co.udea.airline.api.service;

import co.udea.airline.api.model.jpa.model.Passenger;
import co.udea.airline.api.model.jpa.repository.PassengerRepository;
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

    public void saveOrUpdate(Passenger passenger){
        passengerRepository.save(passenger);
    }

   public void delete(Long passengerId){
        passengerRepository.deleteById(passengerId);
    }
}
