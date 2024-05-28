package co.udea.airline.api.controller;


import co.udea.airline.api.model.jpa.model.flightbmodel.DTO.FlightDTO;

import co.udea.airline.api.model.jpa.repository.flightbrepository.IFlightDetailsProjection;
import co.udea.airline.api.model.jpa.repository.flightbrepository.IFlightProjection;
import co.udea.airline.api.services.flightsservices.FlightServices;
import co.udea.airline.api.services.bookingservices.BookingService;
import co.udea.airline.api.model.jpa.model.flightbmodel.Flight;


import co.udea.airline.api.utils.exception.DataNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@PreAuthorize("hasRole('ADMIN')")
//@SecurityRequirement(name = "JWT")
@RequestMapping("/v1/flights")
@Tag(name = "Flight Management", description = "Flight management operations")
public class FlightManagementController {

    @Autowired
    private FlightServices flightService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/searchFlights")
    @Operation(summary = "Search all flights with all information")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Flight.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
    }, description = "List of flights")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public List<FlightDTO> searchFlight() {
        List<Flight> flights = flightService.searchFlights();
        List<FlightDTO> flightsDTO = flights.stream()
                .map(flight -> modelMapper.map(flight, FlightDTO.class))
                .collect(Collectors.toList());
        return flightsDTO;
    }

    @GetMapping("/searchFlightsGeneral")
    @Operation(summary = "Search all flights with general information")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Flight.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
    }, description = "List of flights")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public List<IFlightProjection> searchFlightGeneral() {
        return flightService.searchFlightGeneral();
    }

    @GetMapping("/searchDetails")
    @Operation(summary = "Search one flight with details by flight Number")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Flight.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
    }, description = "List of flights")
    @ApiResponse(responseCode = "400", description = "Invalid")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public List<IFlightDetailsProjection> searchFlightDetailsByFlightNumber(@RequestParam String flightNumber) {
        return flightService.searchFlightDetailsByFlightNumber(flightNumber);
    }

    @GetMapping("/search/{flightNumber}")
    @Operation(summary = "Search one flight with all information by flight Number")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Flight.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
    }, description = "List of flights")
    @ApiResponse(responseCode = "400", description = "Invalid")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public ResponseEntity<FlightDTO> searchFlightById(@PathVariable String flightNumber) {
        Flight flight = flightService.getFlightByFlightNumber(flightNumber);
        if (flight == null) {
            throw new DataNotFoundException("Flight with ID: " + flightNumber + " not found");
        }
        return ResponseEntity.ok(modelMapper.map(flight, FlightDTO.class));
    }



    /**
     * Add a new flight
     *
     * @param flight FlightDTO object with the flight information
     * @return FlightDTO object with the saved flight information
     */
    @PostMapping("/add")
    @Operation(summary = "Add a new flight")
    @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Flight.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
    }, description = "Flight added successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public FlightDTO addFlight(@RequestBody FlightDTO flight) {
        Flight transformedFlight = modelMapper.map(flight, Flight.class);
        Flight savedFlight = flightService.addFlight(transformedFlight);
        return modelMapper.map(savedFlight, FlightDTO.class);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a flight by id")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Flight.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
    }, description = "Flight deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public String deleteFlight(@PathVariable Long id) {

        if (bookingService.flightHasBookings(id)) {
                return "Flight has bookings";
            }

        flightService.deleteFlight(id);
        // if (deleteFlight == null) {
        //     throw new DataNotFoundException("Flight with ID: " + id + " not found");
        // }

        // return ResponseEntity.ok(modelMapper.map(deleteFlight, FlightDTO.class));
        return "The flight has been deleted successfully.";
    }

    @PutMapping("/update/{flightNumber}")
     public ResponseEntity<FlightDTO> updateFlight(@PathVariable String flightNumber,
        @Valid @RequestBody FlightDTO flight) {
        Flight flightRes = modelMapper.map(flight, Flight.class);
        flightRes = flightService.updateFlight(flightNumber, flightRes);
        if (flightRes == null) {
            throw new DataNotFoundException("Flight with flight number: " + flightNumber + " not found");
        }
        FlightDTO flightResDTO = modelMapper.map(flightRes, FlightDTO.class);

        return new ResponseEntity<FlightDTO>(flightResDTO, HttpStatus.CREATED);
    }


}
