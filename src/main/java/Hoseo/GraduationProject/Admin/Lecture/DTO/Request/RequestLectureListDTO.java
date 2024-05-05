package Hoseo.GraduationProject.Admin.Lecture.DTO.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestLectureListDTO {
    @NotEmpty
    @Valid
    private List<RequestLectureDTO> requestLectureDTOList;
}
