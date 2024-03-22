package Hoseo.GraduationProject.Member.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodeDTO {

    @NotBlank
    @Pattern(regexp = "^(\\d{6})$", message = "안증번호는 6자리 또는 8자리이어야 한다.")
    private String code;

    @NotBlank
    @Pattern(regexp = "^(\\d{6}|\\d{8})$", message = "ID는 6자리 또는 8자리이어야 한다.")
    private String id;
}
