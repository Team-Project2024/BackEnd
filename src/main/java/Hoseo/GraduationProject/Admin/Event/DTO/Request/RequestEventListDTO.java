package Hoseo.GraduationProject.Admin.Event.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestEventListDTO {
    private List<RequestEventDTO> requestEventList;
}
