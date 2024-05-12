package co.udea.airline.api.controller;

import java.time.ZoneId;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.dto.JWTResponseDTO;
import co.udea.airline.api.dto.RegisterRequestDTO;
import co.udea.airline.api.service.RegisterService;
import co.udea.airline.api.utils.common.StandardResponse;
import co.udea.airline.api.utils.exception.RegisterException;

@RestController
public class RegisterController {

    private final RegisterService authService;

    public RegisterController(RegisterService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse<JWTResponseDTO>> register(@RequestBody RegisterRequestDTO request) {

        StandardResponse<JWTResponseDTO> sr = new StandardResponse<>();
        try {
            Jwt jwt = authService.register(request);
            sr.setStatus(StandardResponse.StatusStandardResponse.OK.getStatus());
            sr.setMessage("success");
            sr.setBody(new JWTResponseDTO(jwt.getSubject(), jwt.getExpiresAt().atZone(ZoneId.systemDefault()),
                    jwt.getTokenValue()));

            return ResponseEntity.ok().body(sr);
        } catch (RegisterException exception) {
            sr.setStatus(StandardResponse.StatusStandardResponse.ERROR.getStatus());
            sr.setMessage("can't register user");
            sr.setDevMesssage(exception.getMessage());
            sr.setBody(null);

            return ResponseEntity.badRequest().body(sr);
        }
    }

}
