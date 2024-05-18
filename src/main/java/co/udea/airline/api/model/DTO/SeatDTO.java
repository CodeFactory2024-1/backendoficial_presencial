package co.udea.airline.api.model.DTO;

import co.udea.airline.api.utils.common.SeatClassEnum;
import co.udea.airline.api.utils.common.SeatLocationEnum;
import co.udea.airline.api.utils.common.SeatStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {

//    @NotNull
//    @Schema(description = "The seat id auto-generated", requiredMode = RequiredMode.NOT_REQUIRED, example = "1")
//    private Long id = 0L;

    @NotNull
    @Schema(description = "The seat codename used to identify the seat", requiredMode = RequiredMode.REQUIRED, example = "FlightID-SeatNumber-ClassTag")
    private String codename;

    @NotNull
    @Schema(description = "The seat tag. User-friendly label used to label seats.", requiredMode = RequiredMode.REQUIRED, example = "ColumnLetter-RowNumber")
    private String tag;

    @Schema(description = "The status of the seat.", requiredMode = RequiredMode.REQUIRED, example = "AVAILABLE")
    private SeatStatusEnum seatStatus = SeatStatusEnum.AVAILABLE;

    @NotNull(message = "The seat class is required.")
    @Schema(description = "The class of the seat.", requiredMode = RequiredMode.REQUIRED, example = "TOURIST")
    private SeatClassEnum seatClass;

    @NotNull(message = "The seat location is required.")
    @Schema(description = "The location of the seat.", requiredMode = RequiredMode.REQUIRED, example = "WINDOW")
    private SeatLocationEnum seatLocation;

    @NotNull(message = "Surcharge of the seat is required.")
    @Schema(description = "The Surcharge of the seat.", requiredMode = RequiredMode.REQUIRED,
            example = "15000")
    @Min(value = 0, message = "The surcharge must be equal or greater than 0.")
    @Max(value = 1000000, message = "The surcharge must be equal or less than 1000000.")
    private int surcharge = 0;

}
