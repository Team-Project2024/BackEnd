package Hoseo.GraduationProject.API.Lecture.Controller;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.API.Lecture.Service.StudentLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lecture")
public class StudentLectureController {
    private final StudentLectureService studentLectureService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseLectureDTO> getLectureInfo(@PathVariable("id") Long lectureId){
        return ResponseEntity.status(HttpStatus.OK).body(studentLectureService.getLectureInfo(lectureId));
    }
}
