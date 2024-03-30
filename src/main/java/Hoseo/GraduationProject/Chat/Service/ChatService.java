package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
import Hoseo.GraduationProject.Chat.DTO.UserChatDTO;
import Hoseo.GraduationProject.Chat.Domain.ChatBot;
import Hoseo.GraduationProject.Chat.Domain.UserChat;
import Hoseo.GraduationProject.Chat.Repository.UserChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserChatRepository userChatRepository;

    public ResponseChatDTO getChat(String userId){
        List<UserChat> userChats = userChatRepository.findByMemberId(userId);

        // Response DTO를 생성하여 필요한 정보를 매핑합니다.
        ResponseChatDTO responseChatDTO = new ResponseChatDTO();

        // ResponseChatDTO에 들어갈 List 변수 초기화
        List<UserChatDTO> userChatDTOS = new ArrayList<>();
        List<ChatBotDTO> chatBotDTOS = new ArrayList<>();

        for (UserChat userChat : userChats) {
            UserChatDTO userChatDTO = new UserChatDTO();
            ChatBotDTO chatBotDTO = new ChatBotDTO();
            //UserChat에 있는 ChatBot의 답장 내역을 가져옴
            ChatBot chatBot = userChat.getChatBot();

            //UserChatDTO 설정
            userChatDTO.setId(userChat.getId());
            userChatDTO.setContent(userChat.getContent());

            //ChatBotDTO 설정
            chatBotDTO.setId(chatBot.getId());
            chatBotDTO.setContent(chatBot.getContent());

            //List에 add
            userChatDTOS.add(userChatDTO);
            chatBotDTOS.add(chatBotDTO);
        }
        responseChatDTO.setUserChat(userChatDTOS);
        responseChatDTO.setChatBot(chatBotDTOS);

        return responseChatDTO;
    }
}
