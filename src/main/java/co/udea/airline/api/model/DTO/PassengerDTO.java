package co.udea.airline.api.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PassengerDTO {

    @NotNull
    @Schema(description="The Passenger id ", example = "1", requiredMode= Schema.RequiredMode.REQUIRED)
    private String id;

    @NotNull
    @Schema(description="Passenger Name", example = "Carolina", requiredMode= Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull
    @Schema(description="BookingId", example = "1", requiredMode= Schema.RequiredMode.REQUIRED)
    private String bookingId;
}
