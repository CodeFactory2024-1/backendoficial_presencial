package co.udea.airline.api.service;
import java.util.Optional;
import co.udea.airline.api.utils.common.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import co.udea.airline.api.dto.RegisterRequestDTO;
import co.udea.airline.api.model.jpa.model.security.AuthenticationResponse;
import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.model.jpa.repository.security.IdentificationTypeRepository;
import co.udea.airline.api.model.jpa.repository.security.PersonRepository;
import co.udea.airline.api.model.jpa.repository.security.PositionRepository;

@Service
public class AuthenticationService {

    private final PersonRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtUtils jwtUtils;
    private final IdentificationTypeRepository idRepository;
    private final PositionRepository positionRepository;

    public AuthenticationService(PersonRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 IdentificationTypeRepository idRepository,
                                 PositionRepository positionRepository,
                                 AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.idRepository = idRepository;
        this.positionRepository = positionRepository;

        this.jwtUtils = jwtUtils;
    }

    public Person ExternalRegister(Jwt idToken, String LoginSource){
        Person user=new Person();
        user.setEmail(idToken.getClaimAsString("email"));
        user.setFirstName(idToken.getClaimAsString("given_name"));
        user.setLastName(idToken.getClaimAsString("family_name"));
        user.setExternalLoginSource(LoginSource);
        user.setPositions(positionRepository.findByName("USER"));
        user.setVerified(false);
        user.setFailedLoginAttempts(0);
        user.setEnabled(true);
        repository.save(user);
        return user;
    }
    public AuthenticationResponse register(RegisterRequestDTO request) {

        // check if user already exist. if exist than authenticate the user
        if(repository.findByEmail(request.email()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }

        Person user = new Person();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setIdentificationNumber(request.idNumber());
        user.setIdentificationType(idRepository.findByIdentificationType(request.idType()));
        user.setCity(request.city());
        user.setCountry(request.country());
        user.setPhoneNumber(request.phoneNumber());
        user.setGenre(request.genre());
        user.setPositions(positionRepository.findByName("USER"));
        user.setVerified(false);
        user.setFailedLoginAttempts(0);
        user.setEnabled(true);
        user = repository.save(user);
        Jwt jwt = jwtUtils.createToken(user);


        return new AuthenticationResponse(jwt, "User registration was successful");

    }

}