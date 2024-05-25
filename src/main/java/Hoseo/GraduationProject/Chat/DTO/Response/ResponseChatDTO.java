package Hoseo.GraduationProject.Chat.DTO.Response;

import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.UserChatDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseChatDTO {
    List<UserChatDTO> userChat;
    List<ChatBotDTO> chatBot;
}
