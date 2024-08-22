package Hoseo.GraduationProject.API.Lecture.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseLectureDetailListDTO {
    private List<ResponseLectureDetailDTO> responseLectureDetailDTOS;
}
