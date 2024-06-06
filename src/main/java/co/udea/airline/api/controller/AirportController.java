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

@RestController
@RequestMapping("/api/v1/airports")
@CrossOrigin(origins = "*")
@Tag(name = "3 - Airports Data", description = "Get information about airports")
public class AirportController {
  @Autowired
  private AirportService airportService;

  @Autowired
  ModelMapper modelMapper;

  @GetMapping("")
  public List<AirportDTO> getAirports() {
    List<Airport> airports = airportService.getAllAirports();

    List<AirportDTO> response = airports.stream().map(airport -> modelMapper.map(airport, AirportDTO.class))
        .toList();
    response.forEach(airport -> airport
        .add(linkTo(methodOn(AirportController.class).getAirportById(airport.getId())).withSelfRel()));

    return response;
  }

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

  @GetMapping("/countries")
  public List<String> getAirportCountries() {

    return airportService.getAllCountries();
  }

  @GetMapping("/countries/{countryId}")
  public List<AirportDTO> getAirportCountryById(@PathVariable String countryId) {
    List<Airport> airports = airportService.getAirportsByCountry(countryId);

    List<AirportDTO> response = airports.stream().map(airport -> modelMapper.map(airport, AirportDTO.class)).toList();

    response.forEach(airport -> airport
        .add(linkTo(methodOn(AirportController.class).getAirportById(airport.getId())).withSelfRel()));
    return response;
  }

  @GetMapping("/countries/{countryId}/cities")
  public List<String> getAirportCountryCities(@PathVariable String countryId) {

    return airportService.getCitiesByCountry(countryId);
  }

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
