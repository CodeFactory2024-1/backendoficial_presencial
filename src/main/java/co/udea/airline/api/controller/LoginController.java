package co.udea.airline.api.controller;

import java.time.ZoneId;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.dto.JWTResponseDTO;
import co.udea.airline.api.dto.LoginRequestDTO;
import co.udea.airline.api.dto.OAuth2LoginRequestDTO;
import co.udea.airline.api.service.LoginService;
import co.udea.airline.api.utils.common.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class LoginController {

    final LoginService loginService;

    final PasswordEncoder passwordEncoder;

    public LoginController(LoginService loginService, PasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "authenticates a user with its email and raw password")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(examples = {
                    @ExampleObject(value = "{\"email\": \"john.doe@example.com\", \"password\":\"example_password\"}")
            })
    })
    @ApiResponse(responseCode = "200", description = "login succeded")
    @ApiResponse(responseCode = "400", description = "incorrect email or password")
    public ResponseEntity<StandardResponse<JWTResponseDTO>> login(@RequestBody LoginRequestDTO loginRequest) {

        StandardResponse<JWTResponseDTO> sr = new StandardResponse<>();

        try {

            Jwt jwt = loginService.authenticateUser(loginRequest.email(), loginRequest.password());
            sr.setStatus(0);
            sr.setMessage("success");
            sr.setBody(new JWTResponseDTO(jwt.getSubject(), jwt.getExpiresAt().atZone(ZoneId.systemDefault()),
                    jwt.getTokenValue()));

            return ResponseEntity.ok().body(sr);

        } catch (AuthenticationException exception) {

            sr.setStatus(1);
            sr.setMessage("incorrect email or password");
            sr.setDevMesssage(exception.getMessage());
            sr.setBody(null);

            return ResponseEntity.badRequest().body(sr);

        }
    }

    @PostMapping("/login/google")
    @Operation(summary = "through a google idToken allows to authenticate a user, and in case it does not exist, the same is registered")
    @ApiResponse(responseCode = "200", description = "the user was authenticated or registerd using the google idToken")
    @ApiResponse(responseCode = "500", description = "an internal exception ocurred when processing the request")
    public ResponseEntity<StandardResponse<JWTResponseDTO>> loginWithOauth2(
            @RequestBody OAuth2LoginRequestDTO loginRequest) {

        StandardResponse<JWTResponseDTO> sr = new StandardResponse<>();

        try {

            Jwt jwt = loginService.authenticateIdToken(loginRequest.idToken());
            sr.setStatus(0);
            sr.setMessage("success");
            sr.setBody(new JWTResponseDTO(jwt.getSubject(), jwt.getExpiresAt().atZone(ZoneId.systemDefault()),
                    jwt.getTokenValue()));
            return ResponseEntity.ok().body(sr);

        } catch (AuthenticationServiceException exception) {

            sr.setStatus(1);
            sr.setMessage("authentication error");
            sr.setDevMesssage(exception.getMessage());

            return ResponseEntity.internalServerError().body(sr);

        } catch (AuthenticationException exception) {

            sr.setStatus(1);
            sr.setMessage("invalid idToken");
            sr.setDevMesssage(exception.getMessage());

            return ResponseEntity.badRequest().body(sr);

        }
    }

}
