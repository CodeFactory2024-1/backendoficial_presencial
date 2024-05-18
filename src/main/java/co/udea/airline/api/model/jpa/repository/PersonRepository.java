package co.udea.airline.api.model.jpa.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    Person findByRecoveryCode(String recoveryCode);

    Boolean existsByEmail(String email);
    
}
