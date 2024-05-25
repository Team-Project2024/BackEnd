package Hoseo.GraduationProject.Chat.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UserChatDTO {
    private Long id;
    private String content;
    private Timestamp chatDate;
}
