package Hoseo.GraduationProject.Chat.Controller;

import Hoseo.GraduationProject.Chat.DTO.Response.ResponseListChatRoomDTO;
import Hoseo.GraduationProject.Chat.Service.ChatRoomService;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-room")
    public ResponseEntity<Long> createChatRoom(@AuthenticationPrincipal CustomUserDetails member, @RequestParam(required = false) String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomService.createChatRoom(member.getMember(), message));
    }

    @GetMapping("/chat-room")
    public ResponseEntity<ResponseListChatRoomDTO> getChatRoomList(@AuthenticationPrincipal CustomUserDetails member) {
        ResponseListChatRoomDTO responseListChatRoomDTO = chatRoomService.getChatRoomList(member.getMember());
        if(responseListChatRoomDTO.getRepsonseChatRoomDTOList().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseListChatRoomDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseListChatRoomDTO);
    }

    @DeleteMapping("/chat-room")
    public ResponseEntity<Void> deleteChatRoom(@AuthenticationPrincipal CustomUserDetails member, @RequestParam(required = false) Long chatRoomId) {
        chatRoomService.deleteChatRoom(member.getMember(), chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/all/chat-room")
    public ResponseEntity<Void> deleteAllChatRoom(@AuthenticationPrincipal CustomUserDetails member) {
        chatRoomService.deleteAllChatRoom(member.getMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
