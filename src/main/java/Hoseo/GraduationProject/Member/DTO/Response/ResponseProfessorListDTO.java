package Hoseo.GraduationProject.Member.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseProfessorListDTO {
    private List<ResponseProfessorDTO> professorDTOList;
}
