package co.udea.airline.api.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.service.PasswordManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/password")
@Tag(name = "4. Password Management", description = "Password change and recovery endpoints")
public class PasswordManagementController {

    private final PasswordManagementService passwordManagementService;

    @Value("${airline-api.mail.enabled}")
    private boolean mailEnabled;

    public PasswordManagementController(PasswordManagementService passwordManagementService) {
        this.passwordManagementService = passwordManagementService;
    }

    @PostMapping("/recovery")
    @Operation(summary = "Sends a password recovery email to the provided email address if user exists")
    @ApiResponse(responseCode = "200", description = "Email sent if user exists")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> sendPasswordRecovery(@RequestParam String email)
            throws MessagingException, UnsupportedEncodingException {
        if (!mailEnabled)
            return ResponseEntity.status(HttpStatus.LOCKED).body("endpoint disabled");
        return ResponseEntity.ok(passwordManagementService.sendRecoveryPasswCode(email));
    }

    @PatchMapping("/recovery")
    @Operation(summary = "Recovers the user's password based on a code sent to the user's email")
    @ApiResponse(responseCode = "200", description = "Password changed")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> passwordRecovery(@RequestParam String code, @RequestParam String newPassword,
            @RequestParam String email) {
        if (!mailEnabled)
            return ResponseEntity.status(HttpStatus.LOCKED).body("endpoint disabled");
        return ResponseEntity.ok(passwordManagementService.passwRecovery(code, newPassword, email));
    }

    @PatchMapping("/reset")
    @Operation(summary = "Changes the user's password that is authenticated in the JWT")
    @ApiResponse(responseCode = "200", description = "Password changed")
    @ApiResponse(responseCode = "401", description = "User is not authenticated")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("authenticated")
    public ResponseEntity<String> passwordReset(@AuthenticationPrincipal Jwt jwt, @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        return ResponseEntity.ok(passwordManagementService.passwReset(jwt, currentPassword, newPassword));
    }
}
