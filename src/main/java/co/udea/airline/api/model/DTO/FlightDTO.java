package co.udea.airline.api.model.DTO;

import co.udea.airline.api.utils.common.FlightTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightDTO {
    @Schema(description="The flight id auto-generated", example = "1", requiredMode=RequiredMode.NOT_REQUIRED)
    private Long id;

    @Schema(description = "The flight type", requiredMode = RequiredMode.AUTO, example = "Domestic")
    private FlightTypeEnum flightType;
}
