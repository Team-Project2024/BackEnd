package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatRoomDTO;
import Hoseo.GraduationProject.Chat.DTO.UserChatDTO;
import Hoseo.GraduationProject.Chat.Domain.ChatBot;
import Hoseo.GraduationProject.Chat.Domain.ChatRoom;
import Hoseo.GraduationProject.Chat.Domain.UserChat;
import Hoseo.GraduationProject.Chat.ExceptionType.ChatExceptionType;
import Hoseo.GraduationProject.Chat.Repository.ChatBotRepository;
import Hoseo.GraduationProject.Chat.Repository.ChatRoomRepository;
import Hoseo.GraduationProject.Chat.Repository.UserChatRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserChatRepository userChatRepository;
    private final ChatBotRepository chatBotRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Long createChatRoom(Member member){
        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .build();
        chatRoomRepository.save(chatRoom);

        return chatRoom.getId();
    }

    public List<ResponseChatRoomDTO> getChatRoomList(Member member){
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberId(member.getId());
        List<ResponseChatRoomDTO> responseChatRoomDTOList = new ArrayList<>();

        for(ChatRoom chatRoom : chatRooms){
            ResponseChatRoomDTO responseChatRoomDTO = new ResponseChatRoomDTO();
            responseChatRoomDTO.setChatRoomId(chatRoom.getId());
            responseChatRoomDTO.setFirstChat(chatRoom.getFirstChat());
            responseChatRoomDTO.setLastChatDate(chatRoom.getLastChatDate());
            responseChatRoomDTOList.add(responseChatRoomDTO);
        }

        return responseChatRoomDTOList;
    }

    @Transactional
    public String testCreateChat(CustomUserDetails member, String message, Long chatRoomId){
        String answer = "Chat Answer";
        ChatRoom chatroom = chatRoomRepository.findById(chatRoomId).get();
        chatroom.updateLastChatDate(new Timestamp(System.currentTimeMillis()));

        UserChat userChat = UserChat.builder()
                .chatDate(new Timestamp(System.currentTimeMillis()))
                .chatRoom(chatroom)
                .content(message)
                .build();

        ChatBot chatBot = ChatBot.builder()
                .content(answer)
                .userChat(userChat)
                .build();
        try{
            chatRoomRepository.save(chatroom);
            userChatRepository.save(userChat);
            chatBotRepository.save(chatBot);
        } catch (Exception e){
            throw new BusinessLogicException(ChatExceptionType.SAVE_CHAT_ERROR);
        }
        return answer;
    }

    public ResponseChatDTO getChat(Long chatRoomId){
        List<UserChat> userChats = userChatRepository.findByChatRoomId(chatRoomId);

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
            userChatDTO.setChatDate(userChat.getChatDate());

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
