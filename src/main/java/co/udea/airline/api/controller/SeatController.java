package co.udea.airline.api.controller;

import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.DTO.SeatXPassengerDTO;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.model.mapper.SeatMapper;
import co.udea.airline.api.services.seats.service.SeatServiceImpl;
import co.udea.airline.api.utils.common.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Seat", description = "Seat management")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SeatController {

    @Autowired
    SeatServiceImpl seatService;

    @Autowired
    SeatMapper seatMapper;

    @GetMapping("/v1/seats/{seatId}")
    @Operation(summary = "Get Seat by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat is obtained successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "Seat Not found"),
            @ApiResponse(responseCode = "500", description = "Server internal Error")})
    public ResponseEntity<StandardResponse<SeatDTO>> getSeatByIdV1(@PathVariable String seatId) {
        return ResponseEntity.ok(
                new StandardResponse<>(
                        StandardResponse.StatusStandardResponse.OK,
                        "A Seat.",
                        seatService.findSeatById(Long.valueOf(seatId))
                )
        );
    }

    @PostMapping("/v1/seats/generate/{nSeats}/flight/{flightId}")
    @Operation(summary = "Generate Seats given a flight id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seats are generated successfully"),
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
                        String.format("%d Seats generated successfully", seatDTOList.size()),
                        seatDTOList
                )
        );

    }

    @GetMapping("/v1/seats/getAllBy/flight/{flightId}")
    @Operation(summary = "Get List of seats by Flight id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "List of Seats successfully obtained"),
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
    @PostMapping("/v1/seats/assign/{seatId}/passenger/{passengerId}")
    @Operation(summary = "Assign a seat to a passenger")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatXPassengerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seats generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Seat or Passenger doesn't exist."),
            @ApiResponse(responseCode = "409", description = "Passenger already has a seat assigned."),
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

    @GetMapping("/v1/seats/getBy/passenger/{passengerId}")
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

    @PutMapping("/v1/seats/change/{newSeatId}/passenger/{passengerId}")
    @Operation(summary = "Changes Passenger current Seat to a new one.")
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

    @PostMapping("/v1/seats/assignRandomly/passenger/{passengerId}")
    @Operation(summary = "Assign a randomly-selected seat to a passenger.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatXPassengerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "A randomly-assigned seat is obtained."),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Passenger does not exist."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<SeatXPassengerDTO>> assignSeatsRandomlyToPassengerV1(
            @PathVariable("passengerId") String passengerId) {
        SeatXPassengerDTO seatXPassengerDTO  = seatService.assignRandomSeatToPassenger(Long.valueOf(passengerId));
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Seat randomly assigned.",
                        seatXPassengerDTO
                )
        );
    }

    @GetMapping("/v1/seats/booking/{bookingId}/totalSurchargeValue")
    @Operation(summary = "Get the total surcharge for a given booking.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Total surcharge returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Booking does not exist."),
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

    @GetMapping("/v1/seats/{seatId}/surcharge")
    @Operation(summary = "Get seat  surcharge")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Surcharge returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Seat does not exist."),
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

    @PutMapping("/v1/seats/update/{seatId}/surcharge/{newSurcharge}")
    @Operation(summary = "Updates the value of surcharge for a given seat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SeatDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Seat updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Seat not found."),
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
    @DeleteMapping("/v1/seats/remove/{seatId}/passenger/{passengerId}")
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

    @GetMapping("/v1/seats/getAllBy/booking/{bookingId}")
    @Operation(summary = "Get all seats by booking.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "List of Seats by booking successfully returned."),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "Booking does not exist."),
            @ApiResponse(responseCode = "500", description = "Server internal Error.")})
    public ResponseEntity<StandardResponse<List<SeatXPassengerDTO>>> getAllSeatsByBookingIdV1(@PathVariable("bookingId") String bookingId){
        return ResponseEntity.ok(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                        "Relation returned successfully.",
                        seatService.getAllSeatsByBookingId(Long.valueOf(bookingId))));
    }
}
