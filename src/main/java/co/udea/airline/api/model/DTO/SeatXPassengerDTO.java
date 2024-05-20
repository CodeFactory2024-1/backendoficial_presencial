package co.udea.airline.api.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatXPassengerDTO {

//    @NotNull
//    @Schema(description = "The Seat x Passenger id")
//    private Long id;

    @NotNull
    @Schema(description = "The Seat id")
    private Long seatId;

    @NotNull
    @Schema(description = "The Passenger id")
    private Long passengerId;

}