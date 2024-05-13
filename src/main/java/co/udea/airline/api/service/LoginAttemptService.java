package co.udea.airline.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.model.jpa.repository.security.PersonRepository;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 3;

    final PersonRepository personRepository;

    public LoginAttemptService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void loginFailedFor(String email) {
        Optional<Person> op = personRepository.findByEmail(email);

        if (op.isEmpty())
            return;

        Person p = op.get();

        if (!p.isEnabled()) {
            // TODO: send email
            return;
        }

        p.setFailedLoginAttempts(p.getFailedLoginAttempts() + 1);

        if (p.getFailedLoginAttempts() >= MAX_ATTEMPTS) {

            p.setEnabled(false);
            // TODO: send recovvery email
        }
        personRepository.save(p);
    }

    public void loginSucceededFor(String email) {
        Person p = personRepository.findByEmail(email).orElseThrow();
        p.setFailedLoginAttempts(0);
        personRepository.save(p);
    }

}
