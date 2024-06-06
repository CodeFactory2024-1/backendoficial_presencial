package co.udea.airline.api.services.flights.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.udea.airline.api.model.jpa.model.flights.Airport;
import co.udea.airline.api.model.jpa.repository.flights.IAirportRepository;

/**
 * This class represents the service layer for managing airports.
 */
@Service
public class AirportService {
  @Autowired
  private IAirportRepository airportRepository;

  /**
   * Retrieves all airports.
   *
   * @return a list of all airports
   */
  public List<Airport> getAllAirports() {
    return airportRepository.findAll();
  }

  /**
   * Retrieves an airport by its ID.
   *
   * @param id the ID of the airport
   * @return the airport with the specified ID, or null if not found
   */
  public Airport getAirportById(String id) {
    return airportRepository.findById(id).orElse(null);
  }

  /**
   * Retrieves an airport by its IATA code.
   *
   * @param IATACode the IATA code of the airport
   * @return the airport with the specified IATA code, or null if not found
   */
  public Airport getAirportByIATACode(String IATACode) {
    return airportRepository.findByIATA(IATACode);
  }

  /**
   * Retrieves a list of airports in a specific city.
   *
   * @param city the city name
   * @return a list of airports in the specified city
   */
  public List<Airport> getAirportsByCity(String city) {
    return airportRepository.findByCity(city);
  }

  /**
   * Retrieves a list of airports in a specific country.
   *
   * @param country the country name
   * @return a list of airports in the specified country
   */
  public List<Airport> getAirportsByCountry(String country) {
    return airportRepository.findByCountry(country);
  }

  /**
   * Retrieves a list of all cities.
   *
   * @return a list of all cities
   */
  public List<String> getAllCities() {
    return airportRepository.findAllCities();
  }

  /**
   * Retrieves a list of all countries.
   *
   * @return a list of all countries
   */
  public List<String> getAllCountries() {
    return airportRepository.findAllCountries();
  }

  /**
   * Retrieves a list of cities in a specific country.
   *
   * @param country the country name
   * @return a list of cities in the specified country
   */
  public List<String> getCitiesByCountry(String country) {
    return airportRepository.findCitiesByCountry(country);
  }
}
