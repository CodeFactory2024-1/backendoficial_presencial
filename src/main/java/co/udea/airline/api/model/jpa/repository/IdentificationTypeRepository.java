package co.udea.airline.api.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.IdentificationType;

@Repository
public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Integer> {
    IdentificationType findByIdentificationTypeName(String identificationTypeName);
}
