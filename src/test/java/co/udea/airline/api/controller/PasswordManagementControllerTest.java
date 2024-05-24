package co.udea.airline.api.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.model.Position;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import co.udea.airline.api.utils.common.JwtUtils;
import jakarta.mail.internet.MimeMessage;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PasswordManagementControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @SpyBean
    JavaMailSender mailSender;

    Person p;

    @BeforeAll
    void setup() {
        p = Person.builder()
                .email("user@recovery.test")
                .password(passwordEncoder.encode("forgotten_password"))
                .verified(true)
                .enabled(true)
                .build();

        personRepository.save(p);

        doNothing().when(mailSender).send(isA(MimeMessage.class));
    }

    @Test
    @Order(1)
    void testSendPasswordRecovery() throws Exception {

        mockMvc.perform(post("/api/password/recovery")
                .param("email", p.getEmail())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        Person savedP = personRepository.findByEmail(p.getEmail()).orElseThrow();
        assertNotNull(savedP.getRecoveryCode());
        assertFalse(savedP.getRecoveryCode().isEmpty());

        savedP.setRecoveryCode("code_for_testing");
        personRepository.save(savedP);
    }

    @Test
    @Order(2)
    void testPasswordRecovery() throws Exception {

        mockMvc.perform(patch("/api/password/recovery")
                .param("email", p.getEmail())
                .param("code", "code_for_testing")
                .param("newPassword", "newPass123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Person saveP = personRepository.findByEmail(p.getEmail()).orElseThrow();
        assertNull(saveP.getRecoveryCode());
        assertTrue(passwordEncoder.matches("newPass123", saveP.getPassword()));
    }

    @Test
    @Order(3)
    void testPasswordReset() throws Exception {
        p.setPositions(List.of(Position.builder()
                .name("USER")
                .privileges(List.of())
                .build()));

        Jwt jwt = jwtUtils.createToken(p);

        mockMvc.perform(patch("/api/password/reset")
                .header("Authorization", "Bearer %s".formatted(jwt.getTokenValue()))
                .param("currentPassword", "newPass123")
                .param("newPassword", "anotherNewPass")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Person saveP = personRepository.findByEmail(p.getEmail()).orElseThrow();
        assertNull(saveP.getRecoveryCode());
        assertTrue(passwordEncoder.matches("anotherNewPass", saveP.getPassword()));
    }
}
