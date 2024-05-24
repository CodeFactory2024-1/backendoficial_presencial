package co.udea.airline.api.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.model.dto.RegisterRequestDTO;
import co.udea.airline.api.service.RegisterService;
import co.udea.airline.api.utils.exception.AlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "1. Signup", description = "Users creation endpoint")
@RequestMapping("/api")
public class RegisterController {

    private final RegisterService authService;

    @Value("${airline-api.mail.enabled}")
    private boolean mailEnabled;

    public RegisterController(RegisterService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Creates a new user based on the data provided")
    @ApiResponse(responseCode = "200", description = "User created")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request, HttpServletRequest http)
            throws MessagingException, UnsupportedEncodingException {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.badRequest().body("user already exists");
        }
    }

    @GetMapping("/verify")
    @Operation(summary = "Verifies a user recently created with a randomly generated code")
    @ApiResponse(responseCode = "200", description = "User successfuly verified")
    @ApiResponse(responseCode = "400", description = "If the code is not a valid verification code or the user is already verified")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> verifyUser(@RequestParam("code") String code) {
        if (!mailEnabled)
            return ResponseEntity.status(HttpStatus.LOCKED).body("endpoint disabled");

        try {

            if (authService.verify(code)) {
                return ResponseEntity.ok().body("verify successful");
            } else {
                return ResponseEntity.badRequest().body("invalid code");
            }
        } catch (MailException e) {
            return ResponseEntity.internalServerError().body("can't register, try again later");
        }
    }

}
