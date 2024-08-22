package Hoseo.GraduationProject.API.GraduationRequirements.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GRListDTO {
    @NotEmpty
    @Valid
    private List<GRDTO> requestGRList;
}
