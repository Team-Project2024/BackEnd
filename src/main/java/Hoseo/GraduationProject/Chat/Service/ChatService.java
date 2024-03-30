package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
import Hoseo.GraduationProject.Chat.DTO.UserChatDTO;
import Hoseo.GraduationProject.Chat.Domain.ChatBot;
import Hoseo.GraduationProject.Chat.Domain.UserChat;
import Hoseo.GraduationProject.Chat.ExceptionType.ChatExceptionType;
import Hoseo.GraduationProject.Chat.Repository.ChatBotRepository;
import Hoseo.GraduationProject.Chat.Repository.UserChatRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Member.ExceptionType.MemberExceptionType;
import Hoseo.GraduationProject.Member.Repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Transactional
    public String testCreateChat(String memberId,String message){
        String answer = "Chat Answer";
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberExceptionType.NONE_MEMBER));

        UserChat userChat = UserChat.builder()
                .chatDate(new Timestamp(System.currentTimeMillis()))
                .member(member)
                .content(message)
                .build();

        ChatBot chatBot = ChatBot.builder()
                .content(answer)
                .userChat(userChat)
                .build();
        try{
            userChatRepository.save(userChat);
            chatBotRepository.save(chatBot);
        } catch (Exception e){
            throw new BusinessLogicException(ChatExceptionType.SAVE_CHAT_ERROR);
        }
        return answer;
    }

    public ResponseChatDTO getChat(String memberId){
        //여기에서 userchat은 쿼리 하나로 처리되는데 chat_bot의 채팅은 쿼리가 여러번 날아가는 N+1 문제가 있음 이건 어떻게 처리?
        List<UserChat> userChats = userChatRepository.findByMemberId(memberId);

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

    @Transactional
    public void deleteChat(String memberId){
        try{
            // userChat의 ID를 가지는 chatBot을 먼저 삭제
            chatBotRepository.deleteChatBotByMemberId(memberId);
            userChatRepository.deleteUserChatByMemberId(memberId);
        } catch(Exception e){
            throw new BusinessLogicException(ChatExceptionType.DELETE_CHAT_ERROR);
        }
    }
}
