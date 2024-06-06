package co.udea.airline.api.model.DTO.flights;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import co.udea.airline.api.utils.common.FlightTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class FlightDTO extends RepresentationModel<FlightDTO> {

    @Schema(description = "The flight id auto-generated", example = "1", requiredMode = RequiredMode.NOT_REQUIRED)
    private long id;

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

    @Schema(description = "The type of the flight", requiredMode = RequiredMode.NOT_REQUIRED, example = "Domestic")
    private FlightTypeEnum flightType = FlightTypeEnum.Domestic;

    @NotNull(message = "The flight must have at least one scale.")
    @Schema(description = "The scales of the flight, the first scale is the origin and the last scale is the destination", required = true, example = "[{ \"airplaneModel\": { \"id\": \"A320-200\" }, \"originAirport\": { \"id\": \"BOG\" }, \"destinationAirport\": { \"id\": \"MDE\" }, \"departureDate\": \"2024-12-30 23:59:59\", \"arrivalDate\": \"2024-12-31 23:59:59\" }]")
    @Size(min = 1, max = 10, message = "The scales must be between 1 and 10 scales.")
    private List<ScaleDTO> scales;
}