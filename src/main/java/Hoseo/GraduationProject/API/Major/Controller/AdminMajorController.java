package Hoseo.GraduationProject.API.Major.Controller;

import Hoseo.GraduationProject.API.Major.DTO.Page.PageResponse;
import Hoseo.GraduationProject.API.Major.DTO.Request.RequestMajorListDTO;
import Hoseo.GraduationProject.API.Major.DTO.Response.ResponseListMajorDTO;
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
    private final int SIZE = 30;

    // 전공 추가 API
    @PostMapping
    public ResponseEntity<Void> createMajor(@Valid @RequestBody RequestMajorListDTO requestMajorListDTO){
        adminMajorService.createMajor(requestMajorListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseListMajorDTO> getMajorList(){
        ResponseListMajorDTO responseListMajorDTO = adminMajorService.getMajorList();
        if(responseListMajorDTO.getResponseMajorDTOList().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseListMajorDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(adminMajorService.getMajorList());
    }

    @GetMapping
    public ResponseEntity<PageResponse> getMajorList(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(required = false) String keyword){
        PageResponse pageResponse = adminMajorService.getMajorListPaging(page, SIZE, keyword);
        if(pageResponse.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pageResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }
}
