package co.udea.airline.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfoDTO {
    String idType;
    String idNumber;
    String firstName;
    String lastName;
    Character genre;
    LocalDate birthDate;
    String phoneNumber;
    String country;
    String province;
    String city;
    String address;
}
