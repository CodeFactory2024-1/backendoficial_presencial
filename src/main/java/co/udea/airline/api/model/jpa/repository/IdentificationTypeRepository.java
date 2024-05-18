package co.udea.airline.api.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.IdentificationType;
import io.swagger.v3.oas.annotations.tags.Tag;

@Repository
@RepositoryRestResource(path = "idTypes")
@Tag(name = "8. Users Management")
public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Integer> {

    @RestResource(exported = false)
    IdentificationType findByIdentificationTypeName(String identificationTypeName);

}
