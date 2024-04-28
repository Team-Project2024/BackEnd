package Hoseo.GraduationProject.Member.DTO;

import Hoseo.GraduationProject.Domain.Major;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JoinDTO {

    @NotBlank
    @Pattern(regexp = "^(\\d{6}|\\d{8})$", message = "ID는 6자리 또는 8자리이어야 한다.")
    private String id;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "이름 특수문자를 제외한 2~10자리여야 한다.")
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Role role;

    @NotBlank
    private Major major;
}
