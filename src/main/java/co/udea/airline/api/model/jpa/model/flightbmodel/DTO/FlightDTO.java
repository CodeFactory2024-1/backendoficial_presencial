package co.udea.airline.api.model.jpa.model.flightbmodel.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * Represents a flight data transfer object (DTO).
 * This class contains information about a flight, such as its ID, flight number, base price, tax percentage, surcharge, and scales.
 */

@Setter
@Getter
@ToString
@Data
public class FlightDTO {
    /**
     * Unique identifier of the flight.
     */
    @Schema(description = "Unique identifier of the flight.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;
    /**
     * Flight number (Generally it is two letters of the airline and 3 or 4 numbers).
     * Rule of the ICAO (International Civil Aviation Organization) and the IATA (Association
     * International Air Transport). .
     */
    @Schema(description = "The flight Number in IATA format.", example = "SA1234")
    private String flightNumber;
    /**
     * Base price of the flight.
     */
    @Schema(description = "Base price of the flight.",  requiredMode = Schema.RequiredMode.REQUIRED, example = "579000")
    @Min(value = 0, message = "The base price must be greater than 0")
    @Max(value = 1000000, message = "The base price must be less than 1000000.")
    @NotNull(message = "The base price is required.")
     private Double basePrice;
    /**
     * Tax percentage of the flight for payment overruns.
     */
    @NotNull(message = "The tax percent is required.")
    @Schema(description = "Tax percentage of the flight for payment overruns.", example = "19.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 0, message = "The tax percent must be greater than 0")
    @Max(value = 100, message = "The tax percent must be less than 100.")
    private Double taxPercentage;
    /**
     * Surcharge of the flight.
     */
    @NotNull(message = "The surcharge is required.")
    @Schema(description = "The surcharge of the flight", requiredMode = Schema.RequiredMode.REQUIRED, example = "10.5")
    @Min(value = 0, message = "The surcharge must be greater than 0.")
    @Max(value = 100, message = "The surcharge must be less than 100.")
    private Double surcharge;
    /**
     * Scales of the flight.
     * This list contains information about the scales of the flight, including the airplane model,
     * origin and destination airports, departure and arrival dates for each scale.
     */
    @NotNull(message = "The flight must have at least one scale.")
    @Schema(description = "The scales of the flight, the first scale is the origin and the last scale is the destination",requiredMode = Schema.RequiredMode.REQUIRED, required = true, example = "[{ \"airplaneModel\": { \"id\": \"A320-200\" }, \"originAirport\": { \"id\": \"BOG\" }, \"destinationAirport\": { \"id\": \"AEP\" }, \"departureDate\": \"2024-12-30 23:59:59\", \"arrivalDate\": \"2024-12-31 23:59:59\" }]")
    @Size(min = 1, max = 10, message = "The scales must be between 1 and 10 scales.")
    private List<ScaleDTO> scales;

    @Schema(description = "The type of flight", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "International")
    private String flightType;
}
