package co.udea.airline.api.controller;

import java.time.ZoneId;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.model.dto.JWTResponseDTO;
import co.udea.airline.api.model.dto.LoginRequestDTO;
import co.udea.airline.api.model.dto.OAuth2LoginRequestDTO;
import co.udea.airline.api.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "2. Login", description = "Basic login and google login")
@RequestMapping(path = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE })
public class LoginController {

    final LoginService loginService;

    final PasswordEncoder passwordEncoder;

    public LoginController(LoginService loginService, PasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("")
    @Operation(summary = "authenticates a user with its email and raw password")
    @ApiResponse(responseCode = "200", description = "login succeded")
    @ApiResponse(responseCode = "400", description = "incorrect email or password")
    @ApiResponse(responseCode = "500", description = "an internal exception ocurred when processing the request")
    public ResponseEntity<JWTResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {

        Jwt jwt = loginService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        JWTResponseDTO responseDTO = new JWTResponseDTO(jwt.getSubject(),
                jwt.getExpiresAt().atZone(ZoneId.systemDefault()),
                jwt.getTokenValue());

        return ResponseEntity.ok().body(responseDTO);

    }

    @PostMapping("/google")
    @Operation(summary = "through a google idToken allows to authenticate a user, and in case it does not exist, the same is registered")
    @ApiResponse(responseCode = "200", description = "the user was authenticated or registerd using the google idToken")
    @ApiResponse(responseCode = "400", description = "if the idToken is not valid")
    @ApiResponse(responseCode = "500", description = "an internal exception ocurred when processing the request")
    public ResponseEntity<JWTResponseDTO> loginWithOauth2(@RequestBody OAuth2LoginRequestDTO loginRequest) {

        Jwt jwt = loginService.authenticateIdToken(loginRequest.getIdToken());
        JWTResponseDTO responseDTO = new JWTResponseDTO(jwt.getSubject(),
                jwt.getExpiresAt().atZone(ZoneId.systemDefault()),
                jwt.getTokenValue());

        return ResponseEntity.ok().body(responseDTO);

    }

}
