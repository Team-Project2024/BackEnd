package Hoseo.GraduationProject.Member.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserIdDTO {
    @Email
    private String email;

    @NotBlank
    private String name;
}
