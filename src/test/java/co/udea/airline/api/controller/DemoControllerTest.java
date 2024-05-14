package co.udea.airline.api.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.model.jpa.model.security.Position;
import co.udea.airline.api.utils.common.JwtUtils;

@SpringBootTest
// @WebAppConfiguration
// @ContextConfiguration(classes = SecurityConfig.class)
@Profile("test")
@AutoConfigureMockMvc
public class DemoControllerTest {

    // @Autowired
    // WebApplicationContext context;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MockMvc mockMvc;

    Person person;

    @BeforeEach
    void setup() {
        // mockMvc = MockMvcBuilders
        // .webAppContextSetup(context)
        // .defaultRequest(
        // MockMvcRequestBuilders
        // .post("/demo")
        // .contentType(MediaType.APPLICATION_JSON))
        // .apply(SecurityMockMvcConfigurers.springSecurity())
        // .build();

        person = Person.builder()
                .firstName("user")
                .email("user@email")
                .positions(Arrays.asList(new Position(0L, "USER", "Default user", new ArrayList<>())))
                .enabled(true)
                .verified(true)
                .build();

    }

    @Test
    void testDemoWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/demo"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testDemoWithoAuthorization() throws Exception {
        Jwt jwt = jwtUtils.createToken(person);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/demo")
                .header("Authorization", "Bearer %s".formatted(jwt.getTokenValue())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAdminOnlyWithoutAuthorization() throws Exception {
        Jwt jwt = jwtUtils.createToken(person); // USER

        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin_only")
                .header("Authorization", "Bearer %s".formatted(jwt.getTokenValue())))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testAdminOnlyWithAuthorization() throws Exception {
        person.setPositions(Arrays.asList(new Position(1L, "ADMIN", "Default admin", new ArrayList<>())));

        Jwt jwt = jwtUtils.createToken(person); // ADMIN

        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin_only")
                .header("Authorization", "Bearer %s".formatted(jwt.getTokenValue())))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}