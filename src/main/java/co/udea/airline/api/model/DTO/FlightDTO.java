package co.udea.airline.api.model.DTO;

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

    @Schema(description = "The flight number in IATA format", requiredMode = RequiredMode.AUTO, example = "SA1234")
    private String flightNumber;

    @NotNull(message = "The base price is required.")
    @Schema(description = "The base price of the flight", requiredMode = RequiredMode.REQUIRED, example = "1000000")
    @Min(value = 0, message = "The base price must be greater than 0")
    @Max(value = 1000000, message = "The base price must be less than 1000000.")
    private Double basePrice;

    @NotNull(message = "The tax percent is required.")
    @Schema(description = "The tax percent of the flight", requiredMode = RequiredMode.REQUIRED, example = "19.0")
    @Min(value = 0, message = "The tax percent must be greater than 0")
    @Max(value = 100, message = "The tax percent must be less than 100.")
    private Double taxPercent;

    @NotNull(message = "The surcharge is required.")
    @Schema(description = "The surcharge of the flight", requiredMode = RequiredMode.REQUIRED, example = "10.5")
    @Min(value = 0, message = "The surcharge must be greater than 0.")
    @Max(value = 100, message = "The surcharge must be less than 100.")
    private Double surcharge;

}
