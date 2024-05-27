package co.udea.airline.api.controller;
import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/persons")
    public List<Person> getPersons() {
        return (List<Person>) personService.getPersons();
    }

    @GetMapping("/{personId}")
    public Optional<Person> getPersonById(@PathVariable long personId) {
        return personService.getPerson(personId);
    }

    @PostMapping("person")
    public void savePerson(@RequestBody Person person) {
        personService.saveOrUpdate(person);
    }

   /* @DeleteMapping("/{personId}")
    public void deletePerson(@PathVariable Long personId){
        personService.delete(personId);
    }*/
    
   @GetMapping("documentId/{idNumber}")
   public Optional<Person> getPersonByIdNumber(@PathVariable String idNumber) {
       return personService.getPersonByIdNumber(idNumber);
   }
}
