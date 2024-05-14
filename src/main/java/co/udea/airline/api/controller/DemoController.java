package co.udea.airline.api.controller;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.utils.common.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "4. Authorization demos", description = "Basic authorization filter for user and admin roles")
public class DemoController {

    final JwtUtils jwtUtils;

    public DemoController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/demo")
    @Operation(summary = "this is a demostration of a secured endpoint where only users with the role 'USER' can use it")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> demo(@AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity
                .ok("Hello from secured url, your roles are: %s"
                        .formatted(jwtUtils.getRoles(jwt).stream()
                                .collect(Collectors.joining(","))));
    }

    @GetMapping("/admin_only")
    @Operation(summary = "this is a demostration of a secured endpoint where only users with the role 'ADMIN' can use it")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminOnly(@AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok().header("token", jwt.getTokenValue())
                .body("Hello from admin only url, your roles are: %s"
                        .formatted(jwtUtils.getRoles(jwt).stream()
                                .collect(Collectors.joining(", "))));
    }
}
