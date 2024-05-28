package co.udea.airline.api.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingDTO {
    @NotNull
    @Schema(description="The Booking id ", example = "1", requiredMode= Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull
    @Schema(description="The Booking codename ", example = "BookingCodename", requiredMode= Schema.RequiredMode.REQUIRED)
    private String codename;

    @NotNull
    @Schema(description="The Booking flight id ", example = "1", requiredMode= Schema.RequiredMode.REQUIRED)
    private Long flightId;
}
