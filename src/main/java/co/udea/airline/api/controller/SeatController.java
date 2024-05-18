package co.udea.airline.api.controller;

import co.udea.airline.api.model.DTO.CreateSeatDTO;
import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.mapper.SeatMapper;
import co.udea.airline.api.services.seats.service.SeatServiceImpl;
import co.udea.airline.api.utils.common.Messages;
import co.udea.airline.api.utils.common.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Seat", description = "Seat management")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/seat")
public class SeatController {

    @Autowired
    private SeatServiceImpl seatService;

    @Autowired
    private Messages messages;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SeatMapper seatMapper;



    @GetMapping("/v1/find/{id}")
    @Operation(summary = "Get Seat by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Seat.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat obtained successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "Seat Not found"),
            @ApiResponse(responseCode = "500", description = "Server internal Error")})
    public ResponseEntity<StandardResponse<Seat>> getSeatByIdV1(@PathVariable String id) {
        Long idLong = Long.valueOf(id);
        Optional<Seat> seatFound = seatService.findSeatById(idLong);
        if (seatFound.isPresent()){
            Seat seatResponse = seatFound.get();
            return ResponseEntity.ok(new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                    messages.get("seat.findById"),
                    seatResponse));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/v1/save")
    @Operation(summary = "Save a single Seat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat saved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "409", description = "Seat already exists"),
            @ApiResponse(responseCode = "500", description = "Server internal Error")})
    public ResponseEntity<StandardResponse<CreateSeatDTO>> saveV1(@Valid @RequestBody CreateSeatDTO seatToCreate) {
        CreateSeatDTO seatCreated = seatService.save(seatToCreate);
        return ResponseEntity.ok(
                new StandardResponse<>(
                        StandardResponse.StatusStandardResponse.OK,
                        messages.get("seat.save.successful"),
                        seatCreated
                )
        );
    }
    @PutMapping("/v1/update/{id}")
    @Operation(summary = "Update seat by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Seat.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "Seat Not found"),
            @ApiResponse(responseCode = "500", description = "Server internal Error")})
    public ResponseEntity<StandardResponse<Seat>> updateSeatV1(@Valid @RequestBody Seat seat) {
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        messages.get("seat.update.successful"),
                        seatService.update(seat)
                )
        );
    }
    @PostMapping("/v1/generateSeats/{flightId}")
    @Operation(summary = "Get List of seats by Flight id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seats generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Flight does not exist."),
            @ApiResponse(responseCode = "409", description = "Flight already has seats."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<List<SeatDTO>>> generateSeatsByFlightIdV1(@PathVariable("flightId") String flightId) {
        List<Seat> seatList = seatService.generateSeatsByFlightId(Long.valueOf(flightId));
        List<SeatDTO> seatDTOList = seatList.stream()
                .map(seatMapper::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Seats generated successfully",
                        seatDTOList
                )
        );

    }

}
