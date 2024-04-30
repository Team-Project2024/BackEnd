package Hoseo.GraduationProject.Chat.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ResponseChatRoomDTO {
    private Long chatRoomId;
    private Timestamp lastChatDate;
    private String firstChat;
}
