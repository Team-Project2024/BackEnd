package Hoseo.GraduationProject.API.Event.DTO.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestEventListDTO {
    @NotEmpty
    @Valid
    private List<RequestEventDTO> requestEventList;
}
