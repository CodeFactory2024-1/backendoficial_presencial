package co.udea.airline.api.utils.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.Jwt;

import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.model.Position;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Profile("test")
class JwtUtilsTest {

    @Autowired
    JwtUtils jwtUtils;

    Person p;
    Jwt jwt;

    @BeforeAll
    void setup() {
        p = Person.builder()
                .email("user@test")
                .positions(Arrays.asList(Position.builder()
                        .name("USER")
                        .privileges(new ArrayList<>())
                        .build()))
                .build();

        jwt = jwtUtils.createToken(p);
    }

    @Test
    void testCreateToken() {
        assertEquals(jwt.getSubject(), p.getEmail());
        assertTrue(jwtUtils.getAuthorities(jwt).stream()
                .anyMatch(t -> t.getAuthority().contains("USER")));
    }

    @Test
    void testGetToken() {
        Jwt otherJwt = jwtUtils.getToken(jwt.getTokenValue());

        assertEquals(jwt.getSubject(), otherJwt.getSubject());
        assertEquals(jwt.getTokenValue(), otherJwt.getTokenValue());
        assertEquals(jwt.getClaimAsString("roles"), otherJwt.getClaimAsString("roles"));
        assertEquals(jwt.getClaimAsString("privileges"), otherJwt.getClaimAsString("privileges"));
        assertEquals(jwt.getIssuedAt().getEpochSecond(), otherJwt.getIssuedAt().getEpochSecond());
        assertEquals(jwt.getExpiresAt().getEpochSecond(), otherJwt.getExpiresAt().getEpochSecond());

    }

    @Test
    void testValidateToken() {
        assertTrue(jwtUtils.validateToken(jwt));

        StringBuilder newToken = new StringBuilder(jwt.getTokenValue());
        newToken.replace(newToken.length() - 4, newToken.length(), "abcd");
        jwt = new Jwt(newToken.toString(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getHeaders(), jwt.getClaims());
        assertFalse(jwtUtils.validateToken(jwt));
    }
    
}
