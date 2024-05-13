package co.udea.airline.api.controller;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import co.udea.airline.api.dto.JWTResponseDTO;
import co.udea.airline.api.dto.RegisterRequestDTO;
import co.udea.airline.api.service.RegisterService;
import co.udea.airline.api.utils.common.StandardResponse;
import co.udea.airline.api.utils.exception.RegisterException;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Signup", description = "Users creation endpoint")
public class RegisterController {
    @Autowired
    private final RegisterService authService;

    public RegisterController(RegisterService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse<JWTResponseDTO>> register(@RequestBody RegisterRequestDTO request,HttpServletRequest http) {

        StandardResponse<JWTResponseDTO> sr = new StandardResponse<>();
        try {
            Jwt jwt = authService.register(request,getSiteURL(http));
            sr.setStatus(StandardResponse.StatusStandardResponse.OK.getStatus());
            sr.setMessage("success");
            sr.setBody(new JWTResponseDTO(jwt.getSubject(), jwt.getExpiresAt().atZone(ZoneId.systemDefault()),
                    jwt.getTokenValue()));

            return ResponseEntity.ok().body(sr);
        } catch (RegisterException | MessagingException | UnsupportedEncodingException exception) {
            sr.setStatus(StandardResponse.StatusStandardResponse.ERROR.getStatus());
            sr.setMessage("can't register user");
            sr.setDevMesssage(exception.getMessage());
            sr.setBody(null);

            return ResponseEntity.badRequest().body(sr);
        }
    }
    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code) {
        RegisterService registerService = new RegisterService();
        if (registerService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
}
        private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}
