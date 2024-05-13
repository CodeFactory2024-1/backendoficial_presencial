package co.udea.airline.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @Email
    @NotBlank
    @Schema(description = "The user's email to authenticate", example = "john.doe@example.com")
    String email;

    @NotBlank
    @Schema(description = "The user's password to authenticate", example = "example_password")
    String password;
}