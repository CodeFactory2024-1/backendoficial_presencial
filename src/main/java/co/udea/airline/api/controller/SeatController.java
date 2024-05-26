package co.udea.airline.api.controller;

import co.udea.airline.api.model.DTO.CreateSeatDTO;
import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.DTO.SeatXPassengerDTO;
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
@RequestMapping("/api")
public class SeatController {

    @Autowired
    private SeatServiceImpl seatService;

    @Autowired
    private SeatMapper seatMapper;

    @GetMapping("/v1/seat/{id}")
    @Operation(summary = "Get Seat by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Seat.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat obtained successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "Seat Not found"),
            @ApiResponse(responseCode = "500", description = "Server internal Error")})
    public ResponseEntity<StandardResponse<SeatDTO>> getSeatByIdV1(@PathVariable String id) {
        return ResponseEntity.ok(
                new StandardResponse<>(
                        StandardResponse.StatusStandardResponse.OK,
                        "Seat returned.",
                        seatService.findSeatById(Long.valueOf(id))
                )
        );
    }

    @PostMapping("/v1/generate/seats/{nSeats}/flight/{flightId}")
    @Operation(summary = "Generate Seats given a flight id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seats generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Flight does not exist."),
            @ApiResponse(responseCode = "409", description = "Flight already has seats."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<List<SeatDTO>>> generateSeatsByFlightIdV1(@PathVariable("flightId") String flightId,
                                                                                     @PathVariable("nSeats") String nSeats) {
        List<Seat> seatList = seatService.generateSeatsByFlightId(
                Long.valueOf(flightId),
                Integer.valueOf(nSeats));

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

    @GetMapping("/v1/getAllSeats/flight/{flightId}")
    @Operation(summary = "Get List of seats by Flight id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seats successfully obtained"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Flight does not exist."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<List<SeatDTO>>> getAllSeatsByFlightIdV1(@PathVariable("flightId") String flightId) {
        List<Seat> seatList = seatService.getAllSeatsByFlightId(Long.valueOf(flightId));
        List<SeatDTO> seatDTOList = seatList.stream()
                .map(seatMapper::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Seats retrieved successfully",
                        seatDTOList
                )
        );

    }
    @PostMapping("/v1/assign/seat/{seatId}/passenger/{passengerId}")
    @Operation(summary = "Assign a seat to a passenger")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seats generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Flight does not exist."),
            @ApiResponse(responseCode = "409", description = "Flight already has seats."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<SeatXPassengerDTO>> assignSeatToPassengerV1(
            @PathVariable("seatId") String seatId, @PathVariable("passengerId") String passengerId){
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Seat assigned to Passenger successfully",
                        seatService.assignSeatToPassenger(
                                Long.valueOf(seatId),
                                Long.valueOf(passengerId))
                )
        );
    }

    @GetMapping("/v1/getSeat/passenger/{passengerId}")
    @Operation(summary = "Get Seat by Passenger Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatXPassengerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat successfully obtained"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Passenger does not exist."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<SeatXPassengerDTO>> getSeatByPassengerIdV1(@PathVariable("passengerId") String passengerId) {
        SeatXPassengerDTO seatXPassengerDTO = seatService.getSeatByPassengerId(Long.valueOf(passengerId));
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Assigned seat successfully obtained",
                        seatXPassengerDTO)
        );
    }

    @PutMapping("/v1/change/seat/{newSeatId}/passenger/{passengerId}")
    @Operation(summary = "Changes Passenger's  current Seat to a new one.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatXPassengerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Seat or Passenger not found."),
            @ApiResponse(responseCode = "409", description = "Seat already occupied by another passenger."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<SeatXPassengerDTO>> changeAssignedSeatToPassengerV1(
            @PathVariable("newSeatId") String newSeatId, @PathVariable("passengerId") String passengerId){
        SeatXPassengerDTO updatedSeatXPassengerDTO = seatService.changeAssignedSeatToPassenger(Long.valueOf(newSeatId), Long.valueOf(passengerId));
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Seat updated successfully",
                        updatedSeatXPassengerDTO
                )
        );
    }

    @PostMapping("/v1/assignSeatRandomly/passenger/{passengerId}")
    @Operation(summary = "Assign a randomly-selected seat to a passenger.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatXPassengerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat successfully obtained"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Passenger does not exist."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<SeatXPassengerDTO>> assignSeatsRandomlyToPassengerV1(
            @PathVariable("passengerId") String passengerId) {
        SeatXPassengerDTO seatXPassengerDTO  = seatService.assignRandomSeatToPassenger(Long.valueOf(passengerId));
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Random available seat",
                        seatXPassengerDTO
                )
        );
    }

    @GetMapping("/v1/getTotalSurcharge/booking/{bookingId}")
    @Operation(summary = "Get total surcharge for a given booking.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Total surcharge returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Passenger does not exist."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<String>> getTotalSurchargeByBookingV1(@PathVariable("bookingId") String bookingId){
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Total surcharge returned successfully",
                        seatService.getTotalSurchargeByBooking(
                                Long.valueOf(bookingId)
                        )
                )
        );
    }

    @GetMapping("/v1/getSurcharge/seat/{seatId}")
    @Operation(summary = "Get total surcharge for a given booking.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Surcharge returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Passenger does not exist."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<String>> getSeatSurchargeV1(@PathVariable("seatId") String seatId){
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Surcharge returned successfully",
                        seatService.getSeatSurcharge(
                                Long.valueOf(seatId)
                        )
                )
        );
    }

    @PutMapping("/v1/updateSurcharge/seat/{seatId}/surcharge/{newSurcharge}")
    @Operation(summary = "Update a seat assigned to a passenger")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Seat or Passenger not found."),
            @ApiResponse(responseCode = "409", description = "Seat already occupied by another passenger."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<SeatDTO>> updateSeatSurchargeV1(
            @PathVariable("seatId") String seatId, @PathVariable("newSurcharge") String newSurcharge){
        SeatDTO seatDTO = seatService.setSeatSurcharge(Long.valueOf(seatId),newSurcharge);
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Seat updated successfully",
                        seatDTO
                )
        );
    }
    @DeleteMapping("/v1/remove/seat/{seatId}/passenger/{passengerId}")
    @Operation(summary = "Remove the relation between a seat and a passenger")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relation removed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Seat or Passenger not found."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<SeatDTO>> removeSeatToPassengerV1(
            @PathVariable("seatId") String seatId, @PathVariable("passengerId") String passengerId){
        SeatDTO seatDTO = seatService.removeSeatToPassenger(
                Long.valueOf(seatId),
                Long.valueOf(passengerId));

        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Seat removed successfully",
                        seatDTO)
                );
    }
}
