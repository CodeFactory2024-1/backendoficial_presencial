package co.udea.airline.api.service;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.udea.airline.api.model.dto.LoginRequestDTO;
import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.repository.PersonRepository;
import jakarta.mail.internet.MimeMessage;

@SpringBootTest
@AutoConfigureMockMvc
class LoginAttemptServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @SpyBean
    JavaMailSender mailSender;

    ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setup() {
        personRepository.save(Person.builder()
                .email("user@lock.test")
                .password(passwordEncoder.encode("forgotten_pass"))
                .verified(true)
                .enabled(true)
                .failedLoginAttempts(0)
                .build());

        doNothing().when(mailSender).send(isA(MimeMessage.class));
    }

    @Test
    void testAccountLock() throws Exception {

        LoginRequestDTO requestDTO = new LoginRequestDTO("user@lock.test", "invalid_pass");

        for (int i = 0; i < 3; i++) {

            mockMvc.perform(post("/login")
                    .content(om.writeValueAsString(requestDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

        }

        mockMvc.perform(post("/login")
                .content(om.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isLocked());

    }
}
