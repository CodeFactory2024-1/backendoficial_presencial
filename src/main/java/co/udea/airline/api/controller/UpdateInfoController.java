package co.udea.airline.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import co.udea.airline.api.dto.UpdateInfoDTO;
import co.udea.airline.api.service.UpdateInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "3. Info Update", description = "Update info of user with a JWT")
@RequestMapping("/userinfo")
public class UpdateInfoController {
    @Autowired
    private UpdateInfoService updateInfoService;

    public UpdateInfoController(UpdateInfoService updateInfoService) {
        this.updateInfoService = updateInfoService;
    }

    @GetMapping("")
    @Operation(summary = "returns the info of the currently authenticated user with the respective JWT", description = "You should send the JWT as a bearer token in the 'Authorization' header")
    @ApiResponse(responseCode = "401", description = "User isn't authenticated, try again with a JWT")
    @ApiResponse(responseCode = "200", description = "User's info")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<UpdateInfoDTO> bodyRequest(@AuthenticationPrincipal Jwt jwt) {
        
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(updateInfoService.getInfo(jwt));
    }
    
    @PutMapping("")
    @Operation(summary = "updates the info of the currently authenticated user with the respective JWT", description = "You should send the JWT as a bearer token in the 'Authorization' header")
    @ApiResponse(responseCode = "401", description = "User isn't authenticated, try again with a JWT")
    @ApiResponse(responseCode = "200", description = "Info updated")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> updateInfo(@RequestBody UpdateInfoDTO request, @AuthenticationPrincipal Jwt jwt) {
        
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(updateInfoService.updateInfo(request, jwt));
    }

}
