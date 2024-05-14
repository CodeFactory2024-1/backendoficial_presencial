package co.udea.airline.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.airline.api.dto.UpdateInfoDTO;
import co.udea.airline.api.service.UpdateInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "3. UpdateInfo", description = "Update info of user")
@RequestMapping("/userinfo")
public class UpdateInfoController {
    @Autowired
    private UpdateInfoService updateInfoService;

    public UpdateInfoController(UpdateInfoService updateInfoService) {
        this.updateInfoService = updateInfoService;
    }

    @GetMapping("")
    public ResponseEntity<UpdateInfoDTO> bodyRequest(@AuthenticationPrincipal Jwt jwt) {

        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(updateInfoService.getInfo(jwt));
    }

    @PostMapping("")
    public ResponseEntity<String> updateInfo(@RequestBody UpdateInfoDTO request, @AuthenticationPrincipal Jwt jwt) {
        
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(updateInfoService.updateInfo(request, jwt));
    }

}
