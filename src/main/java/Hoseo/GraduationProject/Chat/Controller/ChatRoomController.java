package Hoseo.GraduationProject.Chat.Controller;

import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatRoomDTO;
import Hoseo.GraduationProject.Chat.Service.ChatRoomService;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-room")
    public ResponseEntity<Long> createChatRoom(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomService.createChatRoom(member.getMember()));
    }

    @GetMapping("/chat-room")
    public ResponseEntity<List<ResponseChatRoomDTO>> getChatRoomList(@AuthenticationPrincipal CustomUserDetails member) {
        List<ResponseChatRoomDTO> chatRoomList = chatRoomService.getChatRoomList(member.getMember());
        if(chatRoomList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(chatRoomList);
        }
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomList);
    }

    @DeleteMapping("/chat-room")
    public ResponseEntity<Void> deleteChatRoom(@AuthenticationPrincipal CustomUserDetails member, @RequestParam Long chatRoomId) {
        chatRoomService.deleteChatRoom(member.getMember(), chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/all/chat-room")
    public ResponseEntity<Void> deleteAllChatRoom(@AuthenticationPrincipal CustomUserDetails member) {
        chatRoomService.deleteAllChatRoom(member.getMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
