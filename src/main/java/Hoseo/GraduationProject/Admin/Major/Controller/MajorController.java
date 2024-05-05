package Hoseo.GraduationProject.Admin.Major.Controller;

import Hoseo.GraduationProject.Admin.Major.DTO.Request.RequestMajorListDTO;
import Hoseo.GraduationProject.Admin.Major.DTO.Response.ResponseMajorDTO;
import Hoseo.GraduationProject.Admin.Major.Service.MajorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/major")
public class MajorController {
    private final MajorService majorService;

    // 전공 추가 API
    @PostMapping
    public ResponseEntity<Void> createMajor(@Valid @RequestBody RequestMajorListDTO requestMajorListDTO){
        majorService.createMajor(requestMajorListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseMajorDTO>> getMajorList(){
        return ResponseEntity.status(HttpStatus.OK).body(majorService.getMajorList());
    }
}
