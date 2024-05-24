package co.udea.airline.api.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.model.Position;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import co.udea.airline.api.model.jpa.repository.PositionRepository;
import co.udea.airline.api.utils.filter.JWTTokenFilter;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class PersonsRolesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    JWTTokenFilter tokenFilter;

    Position posUser, posAdmin;
    Person p;

    @BeforeAll
    void setup() {
        posUser = Position.builder().positionId(1L).name("USER").build();
        posAdmin = Position.builder().positionId(2L).name("ADMIN").build();
        p = Person.builder()
                .personId(1)
                .firstName("Juan")
                .positions(List.of(posUser))
                .build();

        positionRepository.saveInternal(posUser);
        personRepository.save(p);
    }

    @Test
    void testGetUsersRoles() throws Exception {

        mockMvc.perform(get("/api/usersroles")
                .header("Authorization", "Bearer adminTokenTest"))
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsStringIgnoringCase(posUser.getName()),
                        containsStringIgnoringCase(posUser.getPositionId().toString()),
                        containsStringIgnoringCase(p.getPersonId().toString()))));
    }

    @Test
    void testGetUserRoles() throws Exception {

        mockMvc.perform(get("/api/usersroles/{id}", p.getPersonId())
                .header("Authorization", "Bearer adminTokenTest"))
                .andExpect(status().isOk());

    }

    @Test
    void testUpdateUserRoles() throws JsonProcessingException, Exception {

        mockMvc.perform(patch("/api/usersroles/{id}", p.getPersonId())
                .header("Authorization", "Bearer adminTokenTest")
                .content(new ObjectMapper()
                        .writeValueAsString(List.of(posAdmin.getPositionId())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
