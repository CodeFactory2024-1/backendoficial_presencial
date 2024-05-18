package co.udea.airline.api.model.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

    @Schema(description = "The id type's name", example = "Cedula")
    String idType;

    @Schema(description = "The id number", example = "654123987")
    String idNumber;

    @Schema(description = "First name", example = "John")
    String firstName;

    @Schema(description = "Last name", example = "Doe")
    String lastName;

    @Schema(description = "User's genere", example = "M")
    Character genre;

    @Schema(description = "User's birthdate", example = "2007-12-03", format = "YYYY-MM-DD")
    LocalDate birthDate;

    @Schema(description = "Phone number", example = "123 1234567")
    String phoneNumber;

    @Schema(description = "User's country", example = "Colombia")
    String country;

    @Schema(description = "User's province", example = "Antioquia")
    String province;

    @Schema(description = "User's city", example = "Medellín")
    String city;

    @Schema(description = "User's address")
    String address;

    @NotNull
    @Email(regexp = "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")
    @Schema(description = "User's email", example = "john.doe@example.com")
    String email;

    @NotBlank
    @Schema(description = "", example = "example_password")
    String password;

}