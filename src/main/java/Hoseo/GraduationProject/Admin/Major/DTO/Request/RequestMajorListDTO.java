package Hoseo.GraduationProject.Admin.Major.DTO.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestMajorListDTO {
    @NotEmpty
    @Valid
    private List<RequestMajorDTO> requestMajorList;
}
