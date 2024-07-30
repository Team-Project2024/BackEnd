package Hoseo.GraduationProject.API.Major.Controller;

import Hoseo.GraduationProject.API.Major.DTO.Page.PageResponse;
import Hoseo.GraduationProject.API.Major.DTO.Request.RequestMajorListDTO;
import Hoseo.GraduationProject.API.Major.Service.AdminMajorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PageResponse> getMajorList(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(required = false) String keyword){
        PageResponse pageResponse = adminMajorService.getMajorList(page, size, keyword);
        if(pageResponse.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pageResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }
}
