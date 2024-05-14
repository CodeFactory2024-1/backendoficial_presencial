package co.udea.airline.api.controller;

import co.udea.airline.api.dto.UpdateInfoDTO;
import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.service.UpdateInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "UpdateInfo", description = "Update info of user")
public class UpdateInfoController {
    @Autowired
    private UpdateInfoService updateInfoService;
    public UpdateInfoController(UpdateInfoService updateInfoService) {
        this.updateInfoService = updateInfoService;
    }
    @PostMapping("/bodyForUpdate")
    public ResponseEntity<UpdateInfoDTO> bodyRequest(@AuthenticationPrincipal Jwt request)  {

    return ResponseEntity.ok(updateInfoService.findPersonForUpdate(request));
    }
    @PostMapping("/updateInfo")
    public ResponseEntity<String> updateInfo(@RequestBody UpdateInfoDTO request,@AuthenticationPrincipal Jwt jwt)  {
    return ResponseEntity.ok(updateInfoService.updateInfo(request,jwt));
    }

}
