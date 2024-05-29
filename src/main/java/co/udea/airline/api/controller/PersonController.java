package co.udea.airline.api.controller;
import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/person")
@CrossOrigin
@Tag(name = "Person")
public class PersonController {
    @Autowired
    private PersonService personService;
    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/persons")
    public List<Person> getPersons(@AuthenticationPrincipal Jwt jwt) {
        return (List<Person>) personService.getPersons();
    }

    @PreAuthorize("hasAuthority('save:booking')")
    @GetMapping("/{personId}")
    public Optional<Person> getPersonById(@AuthenticationPrincipal Jwt jwt,@PathVariable long personId) {
        return personService.getPerson(personId);
    }
    @PreAuthorize("hasAuthority('save:booking')")
    @PostMapping("person")
    public void savePerson(@AuthenticationPrincipal Jwt jwt,@RequestBody Person person) {
        personService.saveOrUpdate(person);
    }

   /*
   @PreAuthorize("hasAuthority('save:booking')")
   @DeleteMapping("/{personId}")
    public void deletePerson(@PathVariable Long personId){
        personService.delete(personId);
    }*/
   @PreAuthorize("hasAuthority('save:booking')")

   @GetMapping("documentId/{idNumber}")
   public Optional<Person> getPersonByIdNumber(@AuthenticationPrincipal Jwt jwt, @PathVariable String idNumber) {
       return personService.getPersonByIdNumber(idNumber);
   }
}
