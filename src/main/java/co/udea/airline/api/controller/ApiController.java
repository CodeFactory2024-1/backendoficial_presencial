package co.udea.airline.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class represents the API controller for the airline API.
 * It handles requests related to the API endpoints.
 */
@RestController
@RequestMapping("/api/v1")
public class ApiController {
  @Autowired
  private FlightController flightController;

  @Autowired
  private AirplaneModelController airplaneModelController;

  @Autowired
  private AirportController airportController;

  /**
   * Retrieves the API links.
   *
   * @return The API links as an Object.
   */
  @GetMapping("")
  public Object getApiLinks() {
    final Map<String, Map<String, Map<String, String>>> response = new HashMap<>();
    final Map<String, Map<String, String>> _links = new HashMap<>();

    Map<String, String> flights = new HashMap<>();
    flights.put("href", linkTo(flightController.getClass()).toString());

    Map<String, String> airplaneModels = new HashMap<>();
    airplaneModels.put("href", linkTo(airplaneModelController.getClass()).toString());

    Map<String, String> airports = new HashMap<>();
    airports.put("href", linkTo(airportController.getClass()).toString());

    _links.put("flights", flights);
    _links.put("airplaneModels", airplaneModels);
    _links.put("airports", airports);

    response.put("_links", _links);

    return response;
  }

}
