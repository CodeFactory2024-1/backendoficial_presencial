package co.udea.airline.api.model.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.Person;
@Repository
@RepositoryRestResource(path = "persons")
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);
    Person findByVerificationCode(String verificationCode);
}
