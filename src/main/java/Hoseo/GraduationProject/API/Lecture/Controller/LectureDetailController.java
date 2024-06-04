package Hoseo.GraduationProject.API.Lecture.Controller;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDetailListDTO;
import Hoseo.GraduationProject.API.Lecture.ExceptionType.LectureExceptionType;
import Hoseo.GraduationProject.API.Lecture.Service.LectureDetailService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lecture/detail")
public class LectureDetailController {
    private final LectureDetailService lectureDetailService;

    @GetMapping({"/{lectureId}", ""})
    public ResponseEntity<ResponseLectureDetailListDTO> getLectureDetail(@PathVariable(value = "lectureId", required = false) Optional<Long> lectureId) {
        if(lectureId.isEmpty()) throw new BusinessLogicException(LectureExceptionType.INVALID_INPUT_VALUE);
        if(lectureDetailService.getLectureDetails(lectureId.get()).getResponseLectureDetailDTOS().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(lectureDetailService.getLectureDetails(lectureId.get()));
    }
}
