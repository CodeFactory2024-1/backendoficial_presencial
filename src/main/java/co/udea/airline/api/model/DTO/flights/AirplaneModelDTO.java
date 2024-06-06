package co.udea.airline.api.model.DTO.flights;

import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Data Transfer Object for Airplane Model.
 * Represents the model of an airplane with its properties.
 */
@Schema(name = "AirplaneModelDTO", description = "Data Transfer Object for Airplane Model")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AirplaneModelDTO extends RepresentationModel<AirplaneModelDTO> {
    @NotEmpty
    private String id;

    private String family;

    private int capacity;

    private long cargoCapacity;

    private long volumeCapacity;
}
