package co.udea.airline.api.model.jpa.model.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class PersonTest {

    Person p0;

    @BeforeEach
    void createPersonExample() {
        p0 = new Person();
        p0.setPersonId(4);
        p0.setFirstName("john");
        p0.setLastName("doe");
        p0.setGenre('M');
        p0.setAddress("some-adress");
        p0.setBirthDate(LocalDate.now());
        p0.setIdentificationType(new IdentificationType(0L, "ID"));
        p0.setIdentificationNumber("123456789");
        p0.setCountry("some-country");
        p0.setProvince("some-province");
        p0.setCity("some-city");
        p0.setEmail("john.doe@example.com");
        p0.setPassword("pass123");
        p0.setVerified(true);
        p0.setEnabled(true);
        p0.setFailedLoginAttempts(0);
        p0.setPositions(Arrays.asList(new Position(0L, "USER", "", null)));
    }

    Person buildSecondPerson() {
        return Person.builder()
                .personId(p0.getPersonId())
                .firstName(p0.getFirstName())
                .lastName(p0.getLastName())
                .genre(p0.getGenre())
                .address(p0.getAddress())
                .birthDate(p0.getBirthDate())
                .identificationType(p0.getIdentificationType())
                .identificationNumber(p0.getIdentificationNumber())
                .country(p0.getCountry())
                .province(p0.getProvince())
                .city(p0.getCity())
                .email(p0.getEmail())
                .password(p0.getPassword())
                .verified(p0.getVerified())
                .enabled(p0.getEnabled())
                .failedLoginAttempts(p0.getFailedLoginAttempts())
                .positions(p0.getPositions())
                .build();
    }

    @Test
    void testBuilder() {
        assertEquals(p0, buildSecondPerson());
    }

    @Test
    void testCanEqual() {
        Position pos = new Position();
        assertFalse(p0.canEqual(pos));
        Person p1 = new Person();
        assertTrue(p0.canEqual(p1));
    }

    @Test
    void testEquals() {
        Person p1 = buildSecondPerson();

        assertTrue(p0.equals(p1));
    }

    @Test
    void testToString() {
        Field[] fields = Person.class.getDeclaredFields();
        String pStr = p0.toString();
        Arrays.stream(fields).forEach(f -> assertTrue(pStr.contains(f.getName())));
    }
}
