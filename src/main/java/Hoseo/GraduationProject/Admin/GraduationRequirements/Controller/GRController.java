package Hoseo.GraduationProject.Admin.GraduationRequirements.Controller;

import Hoseo.GraduationProject.Admin.GraduationRequirements.DTO.RequestGRListDTO;
import Hoseo.GraduationProject.Admin.GraduationRequirements.Service.GRService;
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
public class GRController {
    private final GRService grService;

    @PostMapping
    public ResponseEntity<Void> createGR(@RequestBody RequestGRListDTO requestGRListDTO){
        grService.createGR(requestGRListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
