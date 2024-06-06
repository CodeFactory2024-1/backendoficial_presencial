package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.services.bookings.service.BookingService;
import co.udea.airline.api.services.flights.service.FlightService;
import co.udea.airline.api.utils.common.AuthRequired;
import co.udea.airline.api.utils.exception.DataNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import jakarta.validation.Valid;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

import co.udea.airline.api.model.DTO.flights.FlightDTO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.stream.Collectors;

/**
 * This class represents the controller for managing flights in the airline API.
 * It handles CRUD operations for flights.
 */
@RestController
@RequestMapping("/api/v1/flights")
@CrossOrigin(origins = "*")
@SecurityScheme(name = "JWT", type = SecuritySchemeType.HTTP, scheme = "bearer")
@Tag(name = "1 - Flight Management", description = "CRUD operations for flights")
public class FlightController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FlightService flightService;

    @Autowired
    private BookingService bookingService;

    /**
     * Create a new flight
     * 
     * @param flight
     * 
     * @return ResponseEntity<FlightDTO>
     */
    @PostMapping("")
    @Operation(summary = "Create a new flight", description = "Create a new flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Flight created")
    })
    @AuthRequired()
    public ResponseEntity<FlightDTO> createFlight(@Valid @RequestBody FlightDTO flight) {
        Flight flightRes = modelMapper.map(flight, Flight.class);
        flightRes = flightService.saveFlight(flightRes);
        FlightDTO flightResDTO = modelMapper.map(flightRes, FlightDTO.class);
        flightResDTO.add(linkTo(methodOn(FlightController.class).getFlightById(flightRes.getId())).withSelfRel());

        return new ResponseEntity<FlightDTO>(flightResDTO, HttpStatus.CREATED);
    }

    /**
     * Get allflights from the database
     * 
     * @return ResponseEntity<List<FlightDTO>>
     */
    @Operation(summary = "Get all flights", description = "Get basic information of all flights")
    @GetMapping("")
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        List<Flight> Flight = flightService.getAllFlights();

        List<FlightDTO> flightDTOs = Flight.stream().map(f -> modelMapper.map(f, FlightDTO.class))
                .collect(Collectors.toList());

        flightDTOs.forEach(f -> f.add(linkTo(methodOn(FlightController.class).getFlightById(f.getId())).withSelfRel()));

        return ResponseEntity.ok(flightDTOs);
    }

    /**
     * Get a flight by id
     * 
     * @param id
     * 
     * @return ResponseEntity<FlightDTO>
     */
    @Operation(summary = "Get a flight by id", description = "Returns only one flight by id")
    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable long id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null) {
            throw new DataNotFoundException("Flight with ID: " + id + " not found");
        }

        FlightDTO response = modelMapper.map(flight, FlightDTO.class);

        response.add(linkTo(methodOn(FlightController.class).getFlightById(id)).withSelfRel());

        return ResponseEntity.ok(response);
    }

    /**
     * Search a flight by number. This column is unique
     * 
     * @param flightNumber
     * 
     * @return ResponseEntity<FlightDTO>
     */
    @Operation(summary = "Get a flight with filters", description = "Returns only one flight for given filters")
    @GetMapping("/filter")
    public ResponseEntity<FlightDTO> getFlightByNumber(@RequestParam String flightNumber) {
        Flight flight = flightService.getFlightByFlightNumber(flightNumber);
        if (flight == null) {
            throw new DataNotFoundException("Flight with flight number: " + flightNumber + " not found");
        }

        FlightDTO response = modelMapper.map(flight, FlightDTO.class);

        response.add(linkTo(methodOn(FlightController.class).getFlightById(flight.getId())).withSelfRel());

        return ResponseEntity.ok(response);
    }

    /**
     * Update a flight by its number
     * 
     * @param flightNumber
     * 
     * @param flight
     * 
     * @return ResponseEntity<FlightDTO>
     */
    @Operation(summary = "Update flight by flightNumber", description = "Update flight by its number")
    @PutMapping("/{flightNumber}")
    @AuthRequired()
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable String flightNumber,
            @Valid @RequestBody FlightDTO flight) {
        Flight flightRes = modelMapper.map(flight, Flight.class);
        flightRes = flightService.updateFlight(flightNumber, flightRes);
        if (flightRes == null) {
            throw new DataNotFoundException("Flight with flight number: " + flightNumber + " not found");
        }
        FlightDTO flightResDTO = modelMapper.map(flightRes, FlightDTO.class);

        flightResDTO.add(linkTo(methodOn(FlightController.class).getFlightById(flightRes.getId())).withSelfRel());

        return new ResponseEntity<FlightDTO>(flightResDTO, HttpStatus.CREATED);
    }

    /**
     * Deletes a flight by its flight number.
     *
     * @param flightNumber The flight number of the flight to be deleted.
     * @return The ResponseEntity containing the deleted flight as a FlightDTO.
     * @throws DataNotFoundException if the flight with the specified flight number
     *                               is not found.
     */
    @Operation(summary = "Delete flight by flight number", description = "Delete flight by flight number")
    @DeleteMapping("/{flightNumber}")
    @AuthRequired()
    public ResponseEntity<FlightDTO> deleteByFlightNumber(@PathVariable String flightNumber) {
        Flight deletedFlight = null;
        deletedFlight = flightService.deleteFlightByFlightNumber(flightNumber);
        if (deletedFlight == null) {
            throw new DataNotFoundException("Flight with flight number: " + flightNumber + " not found");
        }

        return ResponseEntity.ok(modelMapper.map(deletedFlight, FlightDTO.class));
    }

    /**
     * Check if a flight has bookings
     * 
     * @param flightId
     * 
     * @return ResponseEntity<Boolean>
     */
    @GetMapping("/has-bookings/{flightId}")
    public ResponseEntity<Boolean> flightHasBookings(@PathVariable Long flightId) {
        return ResponseEntity.ok(bookingService.flightHasBookings(flightId));
    }
}
