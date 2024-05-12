package co.udea.airline.api.service;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.udea.airline.api.dto.RegisterRequestDTO;
import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.model.jpa.repository.security.PersonRepository;
import co.udea.airline.api.utils.exception.RegisterException;

class RegisterServiceTest {
    @Mock
    private PersonRepository repository;

    @InjectMocks
    private RegisterService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerWhenUserAlreadyExist() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("CC", "12345678", "John", "Doe", 'M',
                LocalDate.of(1990, 1, 1), "555-1234", "Colombia", "Bogota", "Bogota", "Colombia", "test@example.com",
                "password");
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(new Person()));

        // Assert
        RegisterException thrown = assertThrowsExactly(RegisterException.class, () -> {
            authenticationService.register(request);
        });

        assertTrue(thrown.getMessage().equalsIgnoreCase("user already exist"));
    }
}