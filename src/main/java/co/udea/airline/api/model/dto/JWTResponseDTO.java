package co.udea.airline.api.model.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTResponseDTO {

    @Email
    @NotBlank
    @Schema(description = "The user's email that was authenticated", example = "john.doe@example.com")
    String email;

    @NotNull
    @Schema(description = "The time in the current system zone when the token expires", example = "2024-05-10T23:18:48.261Z")
    ZonedDateTime expiresAt;

    @NotBlank
    @Schema(description = "The Json Web Token")
    String token;

    public static JWTResponseDTO of(Jwt jwt) {

        String email = jwt.getSubject();
        Instant expiresAt = Optional.ofNullable(jwt.getExpiresAt()).orElse(Instant.MAX);
        String token = jwt.getTokenValue();
        return new JWTResponseDTO(email, expiresAt.atZone(ZoneId.systemDefault()), token);

    }

}
