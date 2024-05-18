package Hoseo.GraduationProject.Chat.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseListChatRoomDTO {
    private List<ResponseChatRoomDTO> repsonseChatRoomDTOList;
}
