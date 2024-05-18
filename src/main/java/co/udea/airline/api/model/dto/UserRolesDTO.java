package co.udea.airline.api.model.dto;

import java.util.List;

import co.udea.airline.api.model.jpa.model.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRolesDTO {

    Integer personId;

    String identificationNumber;

    String firstName;

    String lastName;

    String phoneNumber;

    String email;

    List<Position> positions;

}
