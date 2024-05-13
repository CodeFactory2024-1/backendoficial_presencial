package co.udea.airline.api.model.jpa.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.security.IdentificationType;

@Repository
public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Integer> {
    IdentificationType findByIdentificationType(String identificationType);
}
