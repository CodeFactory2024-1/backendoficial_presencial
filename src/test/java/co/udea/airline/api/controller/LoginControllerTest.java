package co.udea.airline.api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.udea.airline.api.dto.LoginRequestDTO;
import co.udea.airline.api.dto.OAuth2LoginRequestDTO;
import co.udea.airline.api.model.jpa.model.security.IdentificationType;
import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.model.jpa.model.security.Position;
import co.udea.airline.api.model.jpa.model.security.Privilege;
import co.udea.airline.api.model.jpa.repository.security.PersonRepository;

@SpringBootTest
@WebAppConfiguration
@Profile("test")
public class LoginControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean(name = "googleJwtDecoder")
    JwtDecoder googleJwtDecoder;

    @MockBean
    PersonRepository personRepository;

    ObjectMapper om = new ObjectMapper();
    Person personToTest = new Person();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(
                        MockMvcRequestBuilders
                                .post("/login")
                                .contentType(MediaType.APPLICATION_JSON))
                .build();

        personToTest.setEmail("user@test.co");
        personToTest.setPassword(passwordEncoder.encode("pass123"));
        personToTest.setIdentificationType(new IdentificationType(0L, "DNI"));
        personToTest.setIdentificationNumber("123456789");
        personToTest.setFirstName("user");
        personToTest.setPositions(Arrays.asList(
                new Position(0L, "USER", "Default user", new ArrayList<Privilege>())));
        personToTest.setVerified(true);
        personToTest.setEnabled(true);
        personToTest.setFailedLoginAttempts(0);

        when(personRepository.findByEmail(anyString()))
                .then(invocation -> {
                    String email = invocation.getArgument(0, String.class);
                    if (email.equalsIgnoreCase(personToTest.getEmail())) {
                        return Optional.of(personToTest);
                    }
                    return Optional.empty();
                });

    }

    @Test
    public void validUser() throws JsonProcessingException, Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@test.co", "pass123");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/login")
                .content(om.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.body.token").exists());
    }

    @Test
    public void invalidPassword() throws JsonProcessingException, Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@test.co", "invalid_pass");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/login")
                .content(om.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.allOf(
                                Matchers.containsStringIgnoringCase("incorrect"),
                                Matchers.containsStringIgnoringCase("password"))));
    }

    @Test
    public void validIdToken() throws JsonProcessingException, Exception {

        OAuth2LoginRequestDTO validIdToken = new OAuth2LoginRequestDTO(
                "validHeader.validPayload.validSignature");

        // simulates that the token's validation went okay
        when(googleJwtDecoder.decode(anyString())).then(invocation -> {
            String token = invocation.getArgument(0);
            assertTrue(token.equals(validIdToken.idToken()));
            Map<String, Object> claims = new HashMap<>() {
                {
                    put("sub", Long.toString(anyLong()));
                    put("email", personToTest.getEmail());
                    // doesn't matter
                }
            };
            Map<String, Object> headers = new HashMap<>() {
                {
                    put("alg", "HS256");
                    put("typ", "JWT");
                }
            };
            return new Jwt(token, Instant.now(), Instant.now().plusSeconds(60), headers, claims);
        });

        mockMvc.perform(MockMvcRequestBuilders
                .post("/login/google")
                .content(om.writeValueAsString(validIdToken)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.token").exists());

    }

    @Test
    public void invalidIdToken() throws JsonProcessingException, Exception {
        OAuth2LoginRequestDTO invalidIdToken = new OAuth2LoginRequestDTO("invaliddd.id.tokennn");

        // simulates that the token's validation throws an exception
        when(googleJwtDecoder.decode(anyString())).thenThrow(BadJwtException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/login/google")
                .content(om.writeValueAsString(invalidIdToken)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers
                                .content().string(Matchers
                                        .allOf(Matchers.containsStringIgnoringCase("invalid"),
                                                Matchers.containsStringIgnoringCase("token"))));
    }
}