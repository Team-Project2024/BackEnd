package Hoseo.GraduationProject.Admin.Lecture.Controller;

import Hoseo.GraduationProject.Admin.Lecture.DTO.Request.RequestLectureListDTO;
import Hoseo.GraduationProject.Admin.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.Admin.Lecture.ExceptionType.LectureExceptionType;
import Hoseo.GraduationProject.Admin.Lecture.Service.LectureService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<Void> createLecture(@Valid @RequestBody RequestLectureListDTO requestLectureListDTO){
        lectureService.createLecture(requestLectureListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> deleteLecture(@RequestParam(required = false) Long lectureId){
        if(Objects.isNull(lectureId)) throw new BusinessLogicException(LectureExceptionType.INVALID_INPUT_VALUE);
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
