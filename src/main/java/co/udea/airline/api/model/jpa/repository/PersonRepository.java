package co.udea.airline.api.model.jpa.repository;

import co.udea.airline.api.model.jpa.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    Optional<Person> findByidNumber(String idNumber);
}