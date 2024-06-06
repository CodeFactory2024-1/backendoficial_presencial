package co.udea.airline.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.model.DTO.flights.AirplaneModelDTO;
import co.udea.airline.api.model.jpa.model.flights.AirplaneModel;
import co.udea.airline.api.services.flights.service.AirplaneModelService;
import co.udea.airline.api.utils.exception.DataNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * This class is a controller for managing airplane models in the API.
 * It handles requests related to airplane models and provides endpoints
 * to retrieve information about airplane models.
 */
@RestController
@RequestMapping("/api/v1/airplane-models")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET })
@Tag(name = "2 - Airplane Models Data", description = "Get information about airplane models")
public class AirplaneModelController {
  @Autowired
  private AirplaneModelService airplaneModelService;

  @Autowired
  ModelMapper modelMapper;

  /**
   * Retrieves information of all airplane models.
   *
   * @return A list of {@link AirplaneModelDTO} objects representing the airplane
   *         models.
   */
  @Operation(summary = "Get all airplane models", description = "Get information of all airplane models")
  @GetMapping("")
  public List<AirplaneModelDTO> getAirplaneModels() {
    List<AirplaneModel> airplaneModels = airplaneModelService.getAllAirplaneModels();

    List<AirplaneModelDTO> response = airplaneModels.stream()
        .map(airplaneModel -> modelMapper.map(airplaneModel, AirplaneModelDTO.class))
        .collect(Collectors.toList());

    response.forEach(airplaneModel -> airplaneModel.add(
        linkTo(methodOn(AirplaneModelController.class).getAirplaneModelById(airplaneModel.getId())).withSelfRel()));

    return response;
  }

  /**
   * Represents an airplane model data transfer object (DTO).
   * This class is used to transfer data related to an airplane model between the
   * controller and the client.
   */
  @GetMapping("/{id}")
  @Operation(summary = "Get an Airplane model", description = "Get information of a airplane model by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Airplane model found"),
      @ApiResponse(responseCode = "404", description = "Airplane model not found")
  })
  public AirplaneModelDTO getAirplaneModelById(@PathVariable String id) {
    AirplaneModel airplaneModel = airplaneModelService.getAirplaneModelById(id);

    if (airplaneModel == null) {
      throw new DataNotFoundException("Airplane model with ID: " + id + " not found");
    }

    AirplaneModelDTO response = modelMapper.map(airplaneModel, AirplaneModelDTO.class);
    response.add(linkTo(methodOn(AirplaneModelController.class).getAirplaneModelById(id)).withSelfRel());

    return response;
  }

  /**
   * Retrieves all names of airplane model families.
   *
   * @return a list of strings representing the names of airplane model families
   */
  @GetMapping("/families")
  @Operation(summary = "Get all airplane model families", description = "Get all names of airplane model families")
  public List<String> getAirplaneModelFamilies() {
    return airplaneModelService.getAirplaneModelFamilies();
  }

  /**
   * Retrieves a list of airplane models that belong to a specific family.
   *
   * @param familyId The ID of the family.
   * @return A list of {@link AirplaneModelDTO} objects representing the airplane
   *         models.
   */
  @GetMapping("/families/{familyId}")
  @Operation(summary = "Get all airplane models by family", description = "Get information of all airplane models that belong to a family")
  public List<AirplaneModelDTO> getAirplaneModelFamilyById(@PathVariable String familyId) {
    List<AirplaneModel> airplaneModels = airplaneModelService.getAirplaneModelsByFamily(familyId);

    List<AirplaneModelDTO> response = airplaneModels.stream()
        .map(airplaneModel -> modelMapper.map(airplaneModel, AirplaneModelDTO.class))
        .collect(Collectors.toList());

    response.forEach(airplaneModel -> airplaneModel.add(
        linkTo(methodOn(AirplaneModelController.class).getAirplaneModelById(airplaneModel.getId())).withSelfRel()));

    return response;
  }
}
