package co.udea.airline.api.model.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.IdentificationType;
import io.swagger.v3.oas.annotations.tags.Tag;

@Repository
@RepositoryRestResource(path = "idTypes")
@Tag(name = "8. Users Management")
public interface IdentificationTypeRepository extends CrudRepository<IdentificationType, Integer> {

    @RestResource(exported = false)
    IdentificationType findByIdentificationTypeName(String identificationTypeName);

    @RestResource(path = "all", exported = true)
    List<IdentificationType> findAll();

}
