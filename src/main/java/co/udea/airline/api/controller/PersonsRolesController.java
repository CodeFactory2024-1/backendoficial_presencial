package co.udea.airline.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.model.dto.UserRolesDTO;
import co.udea.airline.api.service.PersonRolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usersroles")
@Tag(name = "8. Users Management")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasAuthority('assign:roles')")
public class PersonsRolesController {

    PersonRolesService personRolesService;

    public PersonsRolesController(PersonRolesService personRolesService) {
        this.personRolesService = personRolesService;
    }

    @GetMapping("")
    @Operation(summary = "Gets all users with their basic info and current roles")
    @ApiResponse(responseCode = "200", description = "Returns a list of users with their positions")
    @ApiResponse(responseCode = "403", description = "If not Admin")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<UserRolesDTO>> getUsersRoles() {
        return ResponseEntity.ok().body(personRolesService.getAllPersons());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a user's basic info and current roles by id")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "403", description = "If not Admin")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<UserRolesDTO> getUserRoles(@PathVariable Long id) {
        return ResponseEntity.ok().body(personRolesService.getById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Updates a user's roles by id and a list of positions' ids (see section 6. Roles Management)")
    @ApiResponse(responseCode = "200", description = "Roles updated")
    @ApiResponse(responseCode = "403", description = "If not Admin")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<String> updateUserRoles(@PathVariable Long id, @RequestBody List<Long> positionsIds) {
        personRolesService.updateUserRoles(id, positionsIds);
        return ResponseEntity.ok().body("positions updated for user with id %d".formatted(id));
    }

}
