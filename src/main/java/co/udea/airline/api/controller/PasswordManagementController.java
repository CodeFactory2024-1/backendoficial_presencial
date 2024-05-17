package co.udea.airline.api.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.service.PasswordManagementService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/password")
public class PasswordManagementController {

    private final PasswordManagementService passwordManagementService;

    public PasswordManagementController(PasswordManagementService passwordManagementService) {
        this.passwordManagementService = passwordManagementService;
    }

    @PostMapping("/recovery")
    public ResponseEntity<String> sendPasswordRecovery(@RequestParam String email)
            throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(passwordManagementService.sendRecoveryPasswCode(email));
    }

    @PatchMapping("/recovery")
    public ResponseEntity<String> passwordRecovery(@RequestParam String code, @RequestParam String newPassword,
            @RequestParam String email) {
        return ResponseEntity.ok(passwordManagementService.passwRecovery(code, newPassword, email));
    }

    @PatchMapping("/reset")
    public ResponseEntity<String> passwordReset(@AuthenticationPrincipal Jwt jwt, @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        return ResponseEntity.ok(passwordManagementService.passwReset(jwt, currentPassword, newPassword));
    }
}
