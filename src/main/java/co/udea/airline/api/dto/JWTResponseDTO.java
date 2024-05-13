package co.udea.airline.api.dto;

import java.time.ZonedDateTime;

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

}
