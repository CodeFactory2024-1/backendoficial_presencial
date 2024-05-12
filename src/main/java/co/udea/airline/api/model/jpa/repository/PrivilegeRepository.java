package co.udea.airline.api.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.Privilege;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Repository
@RepositoryRestResource(path = "privileges")
@Tag(name = "5. Privileges Management", description = "CRUD operations for privileges (only for admins)")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ADMIN')")
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

}
