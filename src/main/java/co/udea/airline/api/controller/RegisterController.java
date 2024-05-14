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
@Tag(name = "Signup", description = "Users creation and verification endpoints")
public class RegisterController {
    @Autowired
    private final RegisterService authService;

    public RegisterController(RegisterService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request,HttpServletRequest http) throws MessagingException, UnsupportedEncodingException {
    return ResponseEntity.ok(authService.register(request,getSiteURL(http)));
    }
    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code) {
        if (authService.verify(code)) {
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
