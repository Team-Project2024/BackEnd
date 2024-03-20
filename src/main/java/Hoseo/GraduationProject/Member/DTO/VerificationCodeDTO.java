package Hoseo.GraduationProject.Member.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodeDTO {

    @NotBlank
    private String code;

    @NotBlank
    private String id;
}
