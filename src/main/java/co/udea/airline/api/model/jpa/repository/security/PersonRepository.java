package co.udea.airline.api.model.jpa.repository.security;

import co.udea.airline.api.model.jpa.model.security.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);
    Person findByVerificationCode(String verificationCode);
}
