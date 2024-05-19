package co.udea.airline.api.model.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.Position;
import co.udea.airline.api.model.jpa.projections.WithPrivilegesAndId;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Repository
@RepositoryRestResource(path = "roles", excerptProjection = WithPrivilegesAndId.class)
@Tag(name = "6. Roles Management", description = "CRUD operations for roles (only for admins)")
@ApiResponse(responseCode = "200", description = "OK")
@ApiResponse(responseCode = "401", description = "User is not authenticated")
@ApiResponse(responseCode = "403", description = "User has insufficient permissions")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasAuthority('modify:roles')")
public interface PositionRepository extends CrudRepository<Position, Long> {

    @RestResource(exported = false)
    @PreAuthorize("permitAll()")
    List<Position> findByName(String name);

    @RestResource(exported = false)
    @PreAuthorize("permitAll()")
    default Position saveInternal(Position position) {
        return save(position);
    }

    @RestResource(path = "byName")
    List<Position> findByNameContainingIgnoreCase(String name);

}
