package Hoseo.GraduationProject.API.SchoolLocation;

import Hoseo.GraduationProject.API.SchoolLocation.DTO.ResponseSchoolLocation;
import Hoseo.GraduationProject.API.SchoolLocation.Service.SchoolLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/location/load")
@RequiredArgsConstructor
public class SchoolLocationTestController {

    private final SchoolLocationService schoolLocationService;

    @GetMapping
    public ResponseEntity<List<ResponseSchoolLocation>> get(@RequestBody TestDTO test){
        return ResponseEntity.status(HttpStatus.OK).body(schoolLocationService.getSchoolLocationList(test.getList()));
    }
}
