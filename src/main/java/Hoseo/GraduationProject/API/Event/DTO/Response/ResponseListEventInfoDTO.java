package Hoseo.GraduationProject.API.Event.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseListEventInfoDTO {
    private List<ResponseEventInfoDTO> responseEventInfoDTOList;
}
