package co.udea.airline.api.services.flights.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.udea.airline.api.model.jpa.model.flights.AirplaneModel;
import co.udea.airline.api.model.jpa.repository.flights.IAirplaneModelRepository;

/**
 * This class represents a service for managing airplane models.
 */
@Service
public class AirplaneModelService {
  @Autowired
  private IAirplaneModelRepository airplaneModelRepository;

  /**
   * Retrieves all airplane models.
   * 
   * @return a list of all airplane models
   */
  public List<AirplaneModel> getAllAirplaneModels() {
    return airplaneModelRepository.findAll();
  }

  /**
   * Retrieves an airplane model by its ID.
   * 
   * @param id the ID of the airplane model
   * @return the airplane model with the specified ID, or null if not found
   */
  public AirplaneModel getAirplaneModelById(String id) {
    return airplaneModelRepository.findById(id).orElse(null);
  }

  /**
   * Retrieves all airplane model families.
   * 
   * @return a list of all airplane model families
   */
  public List<String> getAirplaneModelFamilies() {
    return airplaneModelRepository.findAllFamilies();
  }

  /**
   * Retrieves all airplane models belonging to a specific family.
   * 
   * @param familyId the ID of the airplane model family
   * @return a list of all airplane models belonging to the specified family
   */
  public List<AirplaneModel> getAirplaneModelsByFamily(String familyId) {
    return airplaneModelRepository.findByFamily(familyId);
  }
}
