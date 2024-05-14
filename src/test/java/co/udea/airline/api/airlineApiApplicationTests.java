package co.udea.airline.api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("test")
class AirlineApiApplicationTests {

    @Test
    void appStarts() {
        String args[] = new String[0];
        assertDoesNotThrow(() -> AirlineApiApplication.main(args));
    }

}
