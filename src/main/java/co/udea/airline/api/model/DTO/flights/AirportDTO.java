package co.udea.airline.api.model.DTO.flights;

import org.springframework.hateoas.RepresentationModel;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AirportDTO extends RepresentationModel<AirportDTO>{
    @NotEmpty
    private String id;

    private String name;

    private String type;

    private String city;

    private String country;

    private int runways;
}
