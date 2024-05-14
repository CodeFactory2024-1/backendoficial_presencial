package co.udea.airline.api.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.Position;
import co.udea.airline.api.model.jpa.projections.WithPrivilegesAndId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Repository
@RepositoryRestResource(path = "roles", excerptProjection = WithPrivilegesAndId.class)
@Tag(name = "4. Roles Management", description = "CRUD operations for roles (only for admins)")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ADMIN')")
public interface PositionRepository extends JpaRepository<Position, Long> {

    @RestResource(path = "name")
    @Operation(description = "Search roles by name")
    List<Position> findByName(String name);

}
