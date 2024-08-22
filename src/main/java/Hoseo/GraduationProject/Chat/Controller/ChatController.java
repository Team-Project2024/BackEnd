package Hoseo.GraduationProject.Chat.Controller;

import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
import Hoseo.GraduationProject.Chat.ExceptionType.ChatExceptionType;
import Hoseo.GraduationProject.Chat.Service.ChatService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
    * 챗봇에 메시지를 보내고 답변을 받아오는 메서드
    * */
    @PostMapping
    public ResponseEntity<ChatBotDTO> chat(@AuthenticationPrincipal CustomUserDetails member,
                                                     @RequestParam(required = false) String message,
                                                     @RequestParam(required = false) Long chatRoomId) throws IOException {
        if(message.isEmpty()) throw new BusinessLogicException(ChatExceptionType.CHAT_ERROR);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.detectIntentWithLocation(message,chatRoomId,member.getMember()));
    }

    /**
     * 채팅 내역을 가져오는 메서드
     * */
    @GetMapping
    public ResponseEntity<ResponseChatDTO> getChat(@RequestParam(required = false) Long chatRoomId) throws JsonProcessingException {
        if(chatRoomId == null) throw new BusinessLogicException(ChatExceptionType.GET_CHAT_ERROR);
        ResponseChatDTO responseChatDTO = chatService.getChat(chatRoomId);
        if(responseChatDTO.getUserChat().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseChatDTO);
        } else{
            return ResponseEntity.status(HttpStatus.OK).body(responseChatDTO);
        }
    }
}
