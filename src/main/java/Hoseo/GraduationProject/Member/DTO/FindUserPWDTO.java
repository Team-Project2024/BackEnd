package Hoseo.GraduationProject.Member.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserPWDTO {

    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String id;
}
