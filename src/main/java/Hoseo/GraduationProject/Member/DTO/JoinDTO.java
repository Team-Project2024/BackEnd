package Hoseo.GraduationProject.Member.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {
    private String id;
    private String password;
    private String name;
    private String email;
    private Role role;
    private String major;
}
