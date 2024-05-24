package co.udea.airline.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.udea.airline.api.model.dto.UpdateInfoDTO;
import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import co.udea.airline.api.utils.common.JwtUtils;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UpdateInfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PersonRepository personRepository;

    String[] userInfo = { "123456789", "user", "test", "M", "Col", "123456789" };
    Person p;
    UpdateInfoDTO infoDTO;

    @BeforeAll
    void setup() {
        p = Person.builder()
                .email("userupdate@test.com")
                .identificationNumber(userInfo[0])
                .firstName(userInfo[1])
                .lastName(userInfo[2])
                .genre(userInfo[3].charAt(0))
                .country(userInfo[4])
                .phoneNumber(userInfo[5])
                .build();

        personRepository.save(p);
    }

    @Test
    @Order(1)
    void testGetInfo() throws Exception {

        Jwt jwt = jwtUtils.createToken(p);

        MvcResult result = mockMvc.perform(get("/api/userinfo")
                .header("Authorization", "Bearer %s".formatted(jwt.getTokenValue()))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();

        assertTrue(Arrays.stream(userInfo)
                .map(param -> {
                    try {
                        return result.getResponse().getContentAsString().contains(param);
                    } catch (UnsupportedEncodingException e) {
                        return false;
                    }
                })
                .reduce((t, u) -> t && u).orElse(false));

        infoDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), UpdateInfoDTO.class);
    }

    @Test
    @Order(2)
    void testGetInfoNotLoggedIn() throws Exception {

        mockMvc.perform(get("/api/userinfo")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/userinfo")
                .header("Authorization", "Bearer invlid.token.value")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    void testUpdateInfo() throws JsonProcessingException, Exception {
        assertNotNull(infoDTO);

        infoDTO.setFirstName("John");
        infoDTO.setCity("Bog");
        infoDTO.setAddress("something");

        Jwt jwt = jwtUtils.createToken(p);

        mockMvc.perform(put("/api/userinfo")
                .header("Authorization", "Bearer %s".formatted(jwt.getTokenValue()))
                .content(new ObjectMapper().writeValueAsString(infoDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Person newP = personRepository.findByEmail(p.getEmail()).orElseThrow();
        assertEquals(newP.getFirstName(), infoDTO.getFirstName());
        assertEquals(newP.getCity(), infoDTO.getCity());
        assertEquals(newP.getAddress(), infoDTO.getAddress());
    }
}
