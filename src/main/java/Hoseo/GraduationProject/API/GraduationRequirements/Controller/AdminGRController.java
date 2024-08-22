package Hoseo.GraduationProject.API.GraduationRequirements.Controller;

import Hoseo.GraduationProject.API.GraduationRequirements.DTO.GRListDTO;
import Hoseo.GraduationProject.API.GraduationRequirements.Service.AdminGRService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/graduation")
public class AdminGRController {
    private final AdminGRService adminGrService;

    @PostMapping
    public ResponseEntity<Void> createGR(@Valid @RequestBody GRListDTO GRListDTO){
        adminGrService.createGR(GRListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
