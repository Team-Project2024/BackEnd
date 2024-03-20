package Hoseo.GraduationProject.Member.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertifiCode {
    @NotBlank
    private String id;
    @NotBlank
    private String code;
}
