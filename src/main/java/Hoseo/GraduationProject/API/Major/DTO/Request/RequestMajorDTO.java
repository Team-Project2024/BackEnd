package Hoseo.GraduationProject.API.Major.DTO.Request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMajorDTO {
    @NotBlank
    private String department;
    @Nullable
    private String track;
}
