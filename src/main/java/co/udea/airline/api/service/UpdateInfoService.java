package co.udea.airline.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import co.udea.airline.api.dto.UpdateInfoDTO;
import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.model.jpa.repository.security.IdentificationTypeRepository;
import co.udea.airline.api.model.jpa.repository.security.PersonRepository;
import co.udea.airline.api.utils.common.JwtUtils;

@Service
public class UpdateInfoService {
    @Autowired
    private IdentificationTypeRepository idRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    JwtUtils jwtUtils;

    public UpdateInfoDTO getInfo(Jwt jwt) {
        Person person = personRepository.findByEmail(jwt.getSubject()).orElseThrow();
        String identificationType = person.getIdentificationType() != null
                ? person.getIdentificationType().getIdentificationType()
                : null;

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
                person.getAddress());
        return updateInfoDTO;
    }

    public String updateInfo(UpdateInfoDTO request, Jwt jwt) {
        Person user = personRepository.findByEmail(jwt.getSubject()).orElseThrow();
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
