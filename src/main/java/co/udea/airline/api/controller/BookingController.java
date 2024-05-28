package co.udea.airline.api.controller;


import co.udea.airline.api.model.DTO.BookingDTO;
import co.udea.airline.api.model.DTO.PassengerDTO;
import co.udea.airline.api.model.DTO.SeatDTO;
import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.services.bookings.service.BookingServiceImpl;
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

@Tag(name = "Booking", description = "Booking management")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    BookingServiceImpl bookingService;

    @GetMapping("/v1/booking/{bookingId}/passenger/items")
    @Operation(summary = "Get List of passenger by Booking id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of passengers successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "Booking Not found"),
            @ApiResponse(responseCode = "500", description = "Server internal Error")})
    public ResponseEntity<StandardResponse<List<PassengerDTO>>> getSeatByIdV1(@PathVariable String bookingId) {
        return ResponseEntity.ok(
                new StandardResponse<>(
                        StandardResponse.StatusStandardResponse.OK,
                        "List of passengers.",
                        bookingService.getAllPassengerByBookingId(Long.valueOf(bookingId))
                )
        );
    }

    @GetMapping("/v1/booking/{bookingId}")
    @Operation(summary = "Get Booking by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking obtained successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "Booking Not found"),
            @ApiResponse(responseCode = "500", description = "Server internal Error")})
    public ResponseEntity<StandardResponse<BookingDTO>> getBookingByIdV1(@PathVariable String bookingId) {
        return ResponseEntity.ok(
                new StandardResponse<>(
                        StandardResponse.StatusStandardResponse.OK,
                        "A booking.",
                        bookingService.getBookingById(Long.valueOf(bookingId))
                )
        );
    }

}
