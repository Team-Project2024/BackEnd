package Hoseo.GraduationProject.API.Lecture.Controller;

import Hoseo.GraduationProject.API.Lecture.DTO.Page.PageResponse;
import Hoseo.GraduationProject.API.Lecture.DTO.Request.RequestLectureListDTO;
import Hoseo.GraduationProject.API.Lecture.ExceptionType.LectureExceptionType;
import Hoseo.GraduationProject.API.Lecture.Service.AdminLectureService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/lecture")
public class AdminLectureController {
    private final AdminLectureService adminLectureService;

    @GetMapping
    public ResponseEntity<PageResponse> getLectureListPage(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(required = false) String keyword){
        PageResponse response = adminLectureService.getLectureListPage(page, size, keyword);
        if(response.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<Void> createLecture(@Valid @RequestBody RequestLectureListDTO requestLectureListDTO){
        adminLectureService.createLecture(requestLectureListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping({"/{lectureId}", ""})
    public ResponseEntity<Void> deleteLecture(@PathVariable(value = "lectureId", required = false) Optional<Long> lectureId){
        if(lectureId.isEmpty()) throw new BusinessLogicException(LectureExceptionType.INVALID_INPUT_VALUE);
        adminLectureService.deleteLecture(lectureId.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
