package co.udea.airline.api.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.model.dto.RegisterRequestDTO;
import co.udea.airline.api.service.RegisterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "1. Signup", description = "Users creation endpoint")
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
