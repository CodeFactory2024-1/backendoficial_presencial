package co.udea.airline.api.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.model.DTO.flights.AirportDTO;
import co.udea.airline.api.model.jpa.model.flights.Airport;
import co.udea.airline.api.services.flights.service.AirportService;
import co.udea.airline.api.utils.exception.DataNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * This class is a controller that handles HTTP requests related to airports.
 * It provides endpoints to retrieve information about airports, countries, and
 * cities.
 */
@RestController
@RequestMapping("/api/v1/airports")
@CrossOrigin(origins = "*")
@Tag(name = "3 - Airports Data", description = "Get information about airports")
public class AirportController {
  @Autowired
  private AirportService airportService;

  @Autowired
  ModelMapper modelMapper;

  /**
   * Retrieves a list of all airports.
   *
   * @return A list of AirportDTO objects representing the airports.
   */
  @GetMapping("")
  public List<AirportDTO> getAirports() {
    List<Airport> airports = airportService.getAllAirports();

    List<AirportDTO> response = airports.stream().map(airport -> modelMapper.map(airport, AirportDTO.class))
        .toList();
    response.forEach(airport -> airport
        .add(linkTo(methodOn(AirportController.class).getAirportById(airport.getId())).withSelfRel()));

    return response;
  }

  /**
   * Retrieves an AirportDTO object by its ID.
   *
   * @param id The ID of the airport to retrieve.
   * @return The AirportDTO object representing the airport with the specified ID.
   * @throws DataNotFoundException If no airport is found with the specified ID.
   */
  @GetMapping("/{id}")
  public AirportDTO getAirportById(@PathVariable String id) {
    Airport airport = airportService.getAirportById(id);

    if (airport == null) {
      throw new DataNotFoundException("Airport with ID: " + id + " not found");
    }
    AirportDTO aiportRestDTO = modelMapper.map(airport, AirportDTO.class);
    aiportRestDTO.add(linkTo(methodOn(AirportController.class).getAirportById(id)).withSelfRel());

    return aiportRestDTO;
  }

  /**
   * Retrieves a list of countries where airports are located.
   *
   * @return a list of country names
   */
  @GetMapping("/countries")
  public List<String> getAirportCountries() {

    return airportService.getAllCountries();
  }

  /**
   * Retrieves a list of airports in a country based on the country ID.
   *
   * @param countryId The ID of the country.
   * @return A list of AirportDTO objects representing the airports in the
   *         country.
   */
  @GetMapping("/countries/{countryId}")
  public List<AirportDTO> getAirportCountryById(@PathVariable String countryId) {
    List<Airport> airports = airportService.getAirportsByCountry(countryId);

    List<AirportDTO> response = airports.stream().map(airport -> modelMapper.map(airport, AirportDTO.class)).toList();

    response.forEach(airport -> airport
        .add(linkTo(methodOn(AirportController.class).getAirportById(airport.getId())).withSelfRel()));
    return response;
  }

  /**
   * Retrieves a list of cities in a country based on the given country ID.
   *
   * @param countryId the ID of the country
   * @return a list of cities in the country
   */
  @GetMapping("/countries/{countryId}/cities")
  public List<String> getAirportCountryCities(@PathVariable String countryId) {

    return airportService.getCitiesByCountry(countryId);
  }

  /**
   * Retrieves a list of airports in a specific city by its ID.
   *
   * @param cityId The ID of the city.
   * @return A list of AirportDTO objects representing the airports in the city.
   */
  @GetMapping("/cities/{cityId}")
  public List<AirportDTO> getAirportsCountryCityById(@PathVariable String cityId) {
    List<Airport> airports = airportService.getAirportsByCity(cityId);

    List<AirportDTO> response = airports.stream().map(airport -> modelMapper.map(airport, AirportDTO.class)).toList();
    response.forEach(
        airport -> airport
            .add(linkTo(methodOn(AirportController.class).getAirportById(airport.getId())).withSelfRel()));

    return response;
  }
}
