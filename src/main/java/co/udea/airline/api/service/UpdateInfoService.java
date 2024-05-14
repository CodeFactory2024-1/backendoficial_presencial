package co.udea.airline.api.service;

import co.udea.airline.api.dto.UpdateInfoDTO;
import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.model.jpa.repository.security.IdentificationTypeRepository;
import co.udea.airline.api.model.jpa.repository.security.PersonRepository;
import co.udea.airline.api.utils.common.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdateInfoService {
    @Autowired
    private IdentificationTypeRepository idRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    JwtUtils jwtUtils;
        /**
     * Finds the person information for updating based on the JWT.
     *
     * @param jwt the JSON Web Token containing the user's email.
     * @return an UpdateInfoDTO containing the user's current information.
     * @throws NoSuchElementException if no user is found with the provided email.
     */
    public UpdateInfoDTO findPersonForUpdate(Jwt jwt){
        Person person = personRepository.findByEmail(jwt.getSubject()).orElseThrow();
        String identificationType = person.getIdentificationType() != null ? person.getIdentificationType().getIdentificationType() : null;

        UpdateInfoDTO updateInfoDTO = new UpdateInfoDTO(
            identificationType,
            person.getIdentificationNumber(),
            person.getFirstName(),
            person.getLastName(),
            person.getGenre(),
            person.getBirthDate(),
            person.getPhoneNumber(),
            person.getCountry(),
            person.getProvince(),
            person.getCity(),
            person.getAddress()
        );
    return updateInfoDTO;
    }
        /**
     * Updates the user information based on the provided UpdateInfoDTO and JWT.
     *
     * @param request the UpdateInfoDTO containing the new information for the user.
     * @param jwt the JSON Web Token containing the user's email.
     * @return a success message indicating the user update was successful.
     * @throws NoSuchElementException if no user is found with the provided email.
     */
    public String updateInfo(UpdateInfoDTO request,Jwt jwt) {
        Person user=personRepository.findByEmail(jwt.getSubject()).orElseThrow();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setIdentificationNumber(request.getIdNumber());
        user.setIdentificationType(idRepository.findByIdentificationType(request.getIdType()));
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGenre(request.getGenre());
        user = personRepository.save(user);
        personRepository.save(user);
        return ("User update was successful");
    }
}
