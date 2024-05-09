package Hoseo.GraduationProject.API.Major.Controller;

import Hoseo.GraduationProject.API.Major.DTO.Request.RequestMajorListDTO;
import Hoseo.GraduationProject.API.Major.DTO.Response.ResponseMajorDTO;
import Hoseo.GraduationProject.API.Major.Service.AdminMajorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/major")
public class AdminMajorController {
    private final AdminMajorService adminMajorService;

    // 전공 추가 API
    @PostMapping
    public ResponseEntity<Void> createMajor(@Valid @RequestBody RequestMajorListDTO requestMajorListDTO){
        adminMajorService.createMajor(requestMajorListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseMajorDTO>> getMajorList(){
        return ResponseEntity.status(HttpStatus.OK).body(adminMajorService.getMajorList());
    }
}
