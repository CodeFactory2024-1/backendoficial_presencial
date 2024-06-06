package co.udea.airline.api.model.DTO.flights;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Represents a scale in a flight itinerary.
 */
@Data
public class ScaleDTO {
    /**
     * The auto-generated scale ID.
     */
    @Schema(description = "The scale id auto-generated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;

    /**
     * The airplane model for the scale.
     */
    @NotEmpty()
    private AirplaneModelDTO airplaneModel;

    /**
     * The origin airport for the scale.
     */
    @NotEmpty()
    private AirportDTO originAirport;

    /**
     * The destination airport for the scale.
     */
    @NotEmpty()
    private AirportDTO destinationAirport;

    /**
     * The departure date and time for the scale.
     */
    @NotEmpty()
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(example = "2024-12-30 23:59:59", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureDate;

    /**
     * The arrival date and time for the scale.
     */
    @NotEmpty()
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(example = "2024-12-31 23:59:59", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalDate;
}