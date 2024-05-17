package co.udea.airline.api.model.DTO;

import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.utils.common.SeatClassEnum;
import co.udea.airline.api.utils.common.SeatLocationEnum;
import co.udea.airline.api.utils.common.SeatStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSeatDTO {

    @NotNull
    @Schema(description = "The status of the seat.", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "AVAILABLE")
    private SeatStatusEnum seatStatus = SeatStatusEnum.AVAILABLE;

    @NotNull(message = "The seat class is required.")
    @Schema(description = "The class of the seat.", requiredMode = Schema.RequiredMode.REQUIRED, example = "TOURIST")
    private SeatClassEnum seatClass;

    @NotNull(message = "The seat location is required.")
    @Schema(description = "The location of the seat.", requiredMode = Schema.RequiredMode.REQUIRED, example = "WINDOW")
    private SeatLocationEnum seatLocation;

    @NotNull(message = "Surcharge of the seat is required.")
    @Schema(description = "The Surcharge of the seat.", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "15000")
    @Min(value = 0, message = "The surcharge must be equal or greater than 0.")
    @Max(value = 1000000, message = "The surcharge must be equal or less than 1000000.")
    private int surcharge;

    @NotNull(message = "Associated Flight id is required")
    @Schema(description = "Associated Flight id of the seat.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long flightId;

}
