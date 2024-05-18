package co.udea.airline.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInfoDTO {

    @Schema(description = "User's identification type", example = "Cedula")
    String idType;

    @Schema(description = "User's identification number", example = "123456789")
    String idNumber;

    @Schema(description = "User's first name", example = "Johnatan")
    String firstName;

    @Schema(description = "User's last name", example = "Doe")
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

}
