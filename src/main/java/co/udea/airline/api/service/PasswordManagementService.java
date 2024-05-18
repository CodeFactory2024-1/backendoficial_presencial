package co.udea.airline.api.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import jakarta.mail.MessagingException;
import net.bytebuddy.utility.RandomString;

@Service
public class PasswordManagementService {

    private PersonRepository personRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private MailSenderService mailSenderService;

    public PasswordManagementService(PersonRepository personRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, MailSenderService mailSenderService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.mailSenderService = mailSenderService;
    }

    /**
     * Recovers the user's password using the provided verification code and new
     * password.
     *
     * @param verificationCode The code sent to the user for password recovery.
     * @param password         The new password to set for the user.
     * @return A string indicating the result of the recovery process:
     *         "recovery_success" if the recovery was successful,
     *         "recovery_fail" if the user is not found or is already verified.
     */
    public String passwRecovery(String verificationCode, String password, String email) {
        Person user = personRepository.findByEmail(email).orElseThrow();

        if (user == null || !user.isVerified() || verificationCode == null
                || !user.getRecoveryCode().equals(verificationCode)) {
            return "recovery_fail";
        } else {
            user.setRecoveryCode(null);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
            user.setFailedLoginAttempts(0);
            personRepository.save(user);
            return "recovery_success";
        }

    }

    /**
     * Sends a password recovery email to the user with a link to reset their
     * password.
     *
     * @param email The email address of the user to send the recovery code to.
     * @return A string indicating the result of sending the email: "Sending email
     *         success".
     * @throws MessagingException           If there is an error while sending the
     *                                      email.
     * @throws UnsupportedEncodingException If there is an error with the encoding
     *                                      of the email.
     */
    public String sendRecoveryPasswCode(String email) throws UnsupportedEncodingException, MessagingException {
        Optional<Person> p = personRepository.findByEmail(email);
        if (p.isPresent()) {
            Person user = p.get();
            String randomCode = RandomString.make(64);
            user.setRecoveryCode(randomCode);
            personRepository.save(user);
            mailSenderService.sendRecoveryPasswCode(user);
        }
        return "Sending success if email exists";
    }

    /**
     * Resets the user's password using the provided JWT, current password, and new
     * password.
     *
     * @param jwt             The JWT containing the user's roles and permissions.
     * @param currentPassword The current password of the user.
     * @param newPassword     The new password to set for the user.
     * @return A string indicating the result of the password reset process:
     *         "password_reset_success" if the reset was successful,
     *         "password_reset_fail" if the authentication fails.
     */
    public String passwReset(Jwt jwt, String currentPassword, String newPassword) {
        Person user = personRepository.findByEmail(jwt.getSubject()).orElseThrow();
        Authentication auth = new UsernamePasswordAuthenticationToken(jwt.getSubject(), currentPassword);
        auth = authenticationManager.authenticate(auth);

        if (auth.isAuthenticated()) {
            user.setPassword(passwordEncoder.encode(newPassword));
            personRepository.save(user);
            return "password_reset_success";
        } else {
            return "password_reset_fail";
        }

    }
}
