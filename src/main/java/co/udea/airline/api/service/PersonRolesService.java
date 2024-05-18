package co.udea.airline.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import co.udea.airline.api.model.dto.UserRolesDTO;
import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.model.Position;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import co.udea.airline.api.model.jpa.repository.PositionRepository;

@Service
public class PersonRolesService {

    private PersonRepository personRepository;
    private PositionRepository positionRepository;

    public PersonRolesService(PersonRepository personRepository, PositionRepository positionRepository) {
        this.personRepository = personRepository;
        this.positionRepository = positionRepository;
    }

    public List<UserRolesDTO> getAllPersons() {
        return StreamSupport.stream(personRepository.findAll().spliterator(), false)
                .map(p -> new UserRolesDTO(p.getPersonId(),
                        p.getIdentificationNumber(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getPhoneNumber(),
                        p.getEmail(),
                        p.getPositions()))
                .toList();
    }

    public UserRolesDTO getById(Long id) {
        Person p = personRepository.findById(id).orElseThrow();

        return new UserRolesDTO(p.getPersonId(),
                p.getIdentificationNumber(),
                p.getFirstName(),
                p.getLastName(),
                p.getPhoneNumber(),
                p.getEmail(),
                p.getPositions());
    }

    public void updateUserRoles(Long id, List<Long> positionsIds) {
        Person p = personRepository.findById(id).orElseThrow();

        List<Position> positions = new ArrayList<>();
        positionRepository.findAllById(positionsIds).forEach(positions::add);

        p.setPositions(positions);
        personRepository.save(p);
    }

}
