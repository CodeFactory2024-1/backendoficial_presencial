package co.udea.airline.api.model.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import co.udea.airline.api.model.jpa.model.Privilege;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Repository
@RequestMapping("/api")
@RepositoryRestResource(path = "privileges")
@Tag(name = "7. Privileges Management", description = "CRUD operations for privileges (only for admins)")
@ApiResponse(responseCode = "200", description = "OK")
@ApiResponse(responseCode = "401", description = "User is not authenticated")
@ApiResponse(responseCode = "403", description = "User has insufficient permissions")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasAuthority('modify:privileges')")
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

    @RestResource(path = "byName")
    List<Privilege> findByNameContainingIgnoreCase(String name);

}
