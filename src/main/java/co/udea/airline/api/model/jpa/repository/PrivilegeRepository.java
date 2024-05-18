package co.udea.airline.api.model.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.Privilege;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Repository
@RepositoryRestResource(path = "privileges")
@Tag(name = "7. Privileges Management", description = "CRUD operations for privileges (only for admins)")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ADMIN')")
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

    @RestResource(path = "byName")
    List<Privilege> findByNameContainingIgnoreCase(String name);

    @RestResource(path = "all")
    List<Privilege> findAll();
}
