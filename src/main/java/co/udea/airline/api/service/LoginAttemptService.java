package co.udea.airline.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Service
@Slf4j
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;

    private PersonRepository personRepository;

    private MailSenderService mailSenderService;

    public LoginAttemptService(PersonRepository personRepository, MailSenderService mailSenderService) {
        this.personRepository = personRepository;
        this.mailSenderService = mailSenderService;
    }

    public void loginFailedFor(String email) {
        Optional<Person> op = personRepository.findByEmail(email);

        if (op.isEmpty())
            return;

        Person p = op.get();

        // the 4th try sends the email
        if (!p.isEnabled()) {
            try {
                mailSenderService.sendAccountLockedNotification(p);
            } catch (Exception e) {
                log.error("can't send unlock email", e);
            }
            return;
        }

        p.setFailedLoginAttempts(p.getFailedLoginAttempts() + 1);

        if (p.getFailedLoginAttempts() >= MAX_ATTEMPTS) {
            p.setEnabled(false);
            p.setRecoveryCode(RandomString.make(64));
        }
        personRepository.save(p);
    }

    public void loginSucceededFor(String email) {
        Person p = personRepository.findByEmail(email).orElseThrow();
        p.setFailedLoginAttempts(0);
        personRepository.save(p);
    }

}
