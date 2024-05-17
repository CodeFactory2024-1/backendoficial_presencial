package co.udea.airline.api.controller;

import co.udea.airline.api.service.PasswordManagementService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/password")
public class PasswordManagementController {
    @Autowired
    private final PasswordManagementService passwordManagementService;

    public PasswordManagementController(PasswordManagementService passwordManagementService) {this.passwordManagementService = passwordManagementService;}

    @PostMapping("/recovery")
    public ResponseEntity<String>sendPasswordRecovery(@RequestParam String email)
            throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(passwordManagementService.sendRecoveryPasswCode(email));
    }

    @PatchMapping("/recovery")
    public ResponseEntity<String>passwordRecovery(@RequestParam String code,@RequestParam String newPassword,@RequestParam String email){
        return ResponseEntity.ok(passwordManagementService.passwRecovery(code,newPassword,email));
    }
    @PatchMapping("/reset")
    public ResponseEntity<String>passwordReset(@AuthenticationPrincipal Jwt jwt,@RequestParam String currentPassword, @RequestParam String newPassword){
        return ResponseEntity.ok(passwordManagementService.passwReset(jwt,currentPassword,newPassword));
    }
}
