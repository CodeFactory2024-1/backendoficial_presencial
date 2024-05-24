package co.udea.airline.api.service;

import java.io.UnsupportedEncodingException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import co.udea.airline.api.model.dto.RegisterRequestDTO;
import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.repository.IdentificationTypeRepository;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import co.udea.airline.api.model.jpa.repository.PositionRepository;
import co.udea.airline.api.utils.exception.AlreadyExistsException;
import jakarta.mail.MessagingException;
import net.bytebuddy.utility.RandomString;

@Service
public class RegisterService {

    private PersonRepository personRepository;
    private PasswordEncoder passwordEncoder;
    private IdentificationTypeRepository idRepository;
    private PositionRepository positionRepository;
    private MailSenderService mailSenderService;

    public RegisterService(PersonRepository personRepository, PasswordEncoder passwordEncoder,
            IdentificationTypeRepository idRepository, PositionRepository positionRepository,
            MailSenderService mailSenderService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.idRepository = idRepository;
        this.positionRepository = positionRepository;
        this.mailSenderService = mailSenderService;
    }

    public boolean verify(String verificationCode) {

        if (!mailSenderService.isEnabled())
            return false;

        Person user = personRepository.findByRecoveryCode(verificationCode);

        if (user == null || user.getVerified()) {
            return false;
        } else {
            user.setRecoveryCode(null);
            user.setVerified(true);
            personRepository.save(user);

            return true;
        }

    }

    /**
     * Creates a new person in the database using the idToken
     * 
     * @param idToken     The token generated by the external login service
     * @param loginSource The external login source that provided the idToken
     * @throws UnsupportedOperationException if login source is not 'Google' (as for
     *                                       now)
     * @return The succesfully registered person
     */
    public Person externalRegister(Jwt idToken, String loginSource) throws UnsupportedOperationException {

        if (!loginSource.equalsIgnoreCase("google")) {
            throw new UnsupportedOperationException("login source %s is not supported yet".formatted(loginSource));
        }
        Person user = new Person();
        user.setEmail(idToken.getClaimAsString("email"));
        user.setFirstName(idToken.getClaimAsString("given_name"));
        user.setLastName(idToken.getClaimAsString("family_name"));
        user.setExternalLoginSource(loginSource);
        user.setPositions(positionRepository.findByName("USER"));
        user.setVerified(true);
        user.setFailedLoginAttempts(0);
        user.setEnabled(true);
        return personRepository.save(user);
    }

    /**
     * Creates a new person in the database using the provided
     * {@link RegisterRequestDTO}
     * 
     * @param request The info to register the user
     * @return A {@link Jwt} on success
     * @throws AlreadyExistsException if the user is already registered
     */
    public String register(RegisterRequestDTO request)
            throws AlreadyExistsException, MessagingException, UnsupportedEncodingException {

        // check if user already exist. if exist than authenticate the user
        if (personRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistsException("User already exist");
        }

        Person user = new Person();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIdentificationNumber(request.getIdNumber());
        user.setIdentificationType(idRepository.findByIdentificationTypeName(request.getIdType()));
        user.setCountry(request.getCountry());
        user.setProvince(request.getProvince());
        user.setCity(request.getCity());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGenre(request.getGenre());
        user.setPositions(positionRepository.findByName("USER"));
        user.setFailedLoginAttempts(0);
        user.setEnabled(true);

        if (mailSenderService.isEnabled()) {

            String randomCode = RandomString.make(64);
            user.setRecoveryCode(randomCode);
            user.setVerified(false);
            mailSenderService.sendVerificationEmail(user);
        } else {
            user.setVerified(true);
        }

        personRepository.save(user);
        return ("User registration was successful");
    }

}