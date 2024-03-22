package Hoseo.GraduationProject.Member.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserPWDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "이름 특수문자를 제외한 2~10자리여야 한다.")
    private String name;

    @NotBlank
    @Pattern(regexp = "^(\\d{6}|\\d{8})$", message = "ID는 6자리 또는 8자리이어야 한다.")
    private String id;
}
