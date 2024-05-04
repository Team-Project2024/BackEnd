package Hoseo.GraduationProject.Admin.Major.Controller;

import Hoseo.GraduationProject.Admin.Major.DTO.RequestMajorListDTO;
import Hoseo.GraduationProject.Admin.Major.Service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/major")
public class MajorController {
    private final MajorService majorService;

    // 전공 추가 API
    @PostMapping
    public ResponseEntity<Void> createMajor(@RequestBody RequestMajorListDTO requestMajorListDTO){
        majorService.createMajor(requestMajorListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
