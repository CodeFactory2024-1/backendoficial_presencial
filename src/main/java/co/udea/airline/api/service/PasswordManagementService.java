package co.udea.airline.api.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;

@Service
public class PasswordManagementService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
 * Recovers the user's password using the provided verification code and new password.
 *
 * @param verificationCode The code sent to the user for password recovery.
 * @param password         The new password to set for the user.
 * @return A string indicating the result of the recovery process:
 *         "recovery_success" if the recovery was successful,
 *         "recovery_fail" if the user is not found or is already verified.
 */
    public String passwRecovery(String verificationCode, String password,String email) {
        Person user = personRepository.findByVerificationCode(verificationCode);

        if (user == null || !user.isVerified() || user.getEmail()!=email || verificationCode==null) {
            return "recovery_fail";
        } else {
            user.setVerificationCode(null);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
            personRepository.save(user);
            return "recovery_success";
        }

    }
    /**
 * Sends a password recovery email to the user with a link to reset their password.
 *
 * @param email    The email address of the user to send the recovery code to.
 * @return A string indicating the result of sending the email: "Sending email success".
 * @throws MessagingException           If there is an error while sending the email.
 * @throws UnsupportedEncodingException If there is an error with the encoding of the email.
 */
    public String sendRecoveryPasswCode(String email)
            throws MessagingException, UnsupportedEncodingException {
        Person user=personRepository.findByEmail(email).orElseThrow();
        String toAddress = email;
        String fromAddress = "sitassingapurairlines@gmail.com";
        String senderName = "Sitas airline";
        String subject = "Recover your password";
        String content = "Dear [[name]],<br>"
                + "This is the code to recover your password:<br>"
                + "<h3>[[CODE]]</h3>"
                + "Thank you,<br>"
                + "Sitas.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String fullName = user.getFirstName() + " " + user.getLastName();
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        content = content.replace("[[name]]", (fullName));

        String code = user.getVerificationCode();

        content = content.replace("[[CODE]]", code);

        helper.setText(content, true);

        mailSender.send(message);
        return "Sending email success";
    }
    /**
 * Resets the user's password using the provided JWT, current password, and new password.
 *
 * @param jwt            The JWT containing the user's roles and permissions.
 * @param currentPassword The current password of the user.
 * @param newPassword     The new password to set for the user.
 * @return A string indicating the result of the password reset process:
 *         "password_reset_success" if the reset was successful,
 *         "password_reset_fail" if the authentication fails.
 */
    public String passwReset(Jwt jwt,String currentPassword,String newPassword) {
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
