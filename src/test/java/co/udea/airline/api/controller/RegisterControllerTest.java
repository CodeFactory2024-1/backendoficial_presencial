package co.udea.airline.api.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import co.udea.airline.api.model.dto.OAuth2LoginRequestDTO;
import co.udea.airline.api.model.dto.RegisterRequestDTO;
import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegisterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @SpyBean
    JavaMailSender mailSender;

    @SpyBean(name = "googleJwtDecoder")
    JwtDecoder googleJwtDecoder;

    ObjectMapper om;
    RegisterRequestDTO registerRequestDTO;

    @BeforeAll
    void setup() throws UnsupportedEncodingException, MessagingException {
        registerRequestDTO = RegisterRequestDTO.builder()
                .idType("DNI")
                .idNumber("12345689")
                .firstName("Juan")
                .lastName("Lopez")
                .genre('M')
                .birthDate(LocalDate.parse("2024-05-13"))
                .phoneNumber("456321789")
                .country("Colombia")
                .province("Antioquia")
                .city("Medellín")
                .residence("noc")
                .email("juan@lopez.com")
                .password("pass123")
                .build();

        om = JsonMapper.builder()
                .findAndAddModules()
                .build();

        doNothing().when(mailSender).send(isA(MimeMessage.class));
        doCallRealMethod().when(mailSender).createMimeMessage();
    }

    @Test
    @Order(1)
    void testRegister() throws JsonProcessingException, Exception {

        mockMvc.perform(post("/register")
                .content(om.writeValueAsString(registerRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsStringIgnoringCase("success")));

        assertTrue(personRepository.existsByEmail(registerRequestDTO.getEmail()));

        Person p = personRepository.findByEmail(registerRequestDTO.getEmail()).get();
        assertFalse(p.getVerificationCode().isBlank());

        // set custom code for testing
        p.setVerificationCode("test_code");
        personRepository.save(p);

    }

    @Test
    @Order(2)
    void testRegisterWhenAlreadyRegistered() throws JsonProcessingException, Exception {

        mockMvc.perform(post("/register")
                .content(om.writeValueAsString(registerRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        allOf(containsStringIgnoringCase("already"),
                                containsStringIgnoringCase("exist"))));
    }

    @Test
    @Order(3)
    void testVerifyUser() throws Exception {

        mockMvc.perform(post("/verify")
                .param("code", "test_code")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(4)
    void testVerifyUserWithInvalidCode() throws Exception {

        mockMvc.perform(post("/verify")
                .param("code", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(
                        containsStringIgnoringCase("invalid"),
                        containsStringIgnoringCase("code"))));

    }

    @Test
    void testExternalRegister() throws JsonProcessingException, Exception {

        Jwt customJwt = new Jwt("any.token.value", Instant.now(), Instant.MAX,
                Map.of("alg", "RS256"),
                Map.of("email", "user@test.com", "given_name", "Juan", "family_name", "Lopez"));

        doReturn(customJwt).when(googleJwtDecoder).decode(anyString());

        mockMvc.perform(post("/login/google")
                .content(om.writeValueAsString(new OAuth2LoginRequestDTO(customJwt.getTokenValue())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
