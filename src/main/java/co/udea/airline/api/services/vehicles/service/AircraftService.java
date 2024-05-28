package co.udea.airline.api.services.vehicles.service;

import co.udea.airline.api.model.jpa.model.vehicles.Aircraft;

import co.udea.airline.api.utils.exception.BusinessException;
import co.udea.airline.api.utils.common.Messages;
import co.udea.airline.api.model.jpa.repository.vehicles.AircraftRepository;
import co.udea.airline.api.utils.exception.DataDuplicatedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AircraftService {

    private final AircraftRepository vehicleRepository;
    private final Messages messages;

    public AircraftService(AircraftRepository vehicleRepository, Messages messages) {
        this.vehicleRepository = vehicleRepository;
        this.messages = messages;
    }

    public Aircraft save(Aircraft aircraft) {
        Optional<Aircraft> vehicleOptional = vehicleRepository.findByName(aircraft.getName());
        if (vehicleOptional.isPresent()) {
            throw new DataDuplicatedException(String.format(messages.get("vehicle.save.duplicate.name"), aircraft.getName()));
        }
        return vehicleRepository.save(aircraft);
    }

    public List<Aircraft> findByAll() {
        return vehicleRepository.findAll();
    }

    public Page<Aircraft> getWithPage(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    public Aircraft update(Aircraft vehicle) {
        Optional<Aircraft> vehicleOptional = vehicleRepository.findById(vehicle.getId());
        if (vehicleOptional.isEmpty()) {
            throw new BusinessException(String.format(messages.get("vehicle.update.does.not.exist")));
        }
        return vehicleRepository.save(vehicle);
    }

    public void delete(Integer id) {
        vehicleRepository.findById(id).orElseThrow(() ->
                new BusinessException(String.format(messages.get("vehicle.delete.find.error"), id)));
        this.vehicleRepository.deleteById(id);
    }

}
