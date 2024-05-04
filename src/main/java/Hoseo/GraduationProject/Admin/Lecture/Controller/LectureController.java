package Hoseo.GraduationProject.Admin.Lecture.Controller;

import Hoseo.GraduationProject.Admin.Lecture.DTO.Request.RequestLectureListDTO;
import Hoseo.GraduationProject.Admin.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.Admin.Lecture.Service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/lecture")
public class LectureController {
    private final LectureService lectureService;

    @GetMapping
    public ResponseEntity<List<ResponseLectureDTO>> getLectureList(){
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureList());
    }

    @PostMapping
    public ResponseEntity<Void> createLecture(@RequestBody RequestLectureListDTO requestLectureListDTO){
        lectureService.createLecture(requestLectureListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> deleteLecture(@RequestParam Long lectureId){
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
