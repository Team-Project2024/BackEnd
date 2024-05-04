package Hoseo.GraduationProject.Admin.Lecture.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestLectureListDTO {
    private List<RequestLectureDTO> requestLectureDTOList;
}
