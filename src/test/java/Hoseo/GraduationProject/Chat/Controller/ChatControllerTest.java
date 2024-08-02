package Hoseo.GraduationProject.Chat.Controller;

import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
import Hoseo.GraduationProject.Chat.DTO.UserChatDTO;
import Hoseo.GraduationProject.Chat.Service.ChatService;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("채팅 Post - 실패")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void chat_Fail() throws Exception {
        Member member = new Member();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        String message = "";
        Long chatRoomId = 1L;

        mvc.perform(post("/api/chat")
                        .param("message", message)
                        .param("chatRoomId", String.valueOf(chatRoomId))
                        .contentType("application/json")
                        .with(user(customUserDetails))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(chatService, never()).detectIntentWithLocation(anyString(), anyLong(), any(Member.class));
    }


    @Test
    @DisplayName("채팅 Post - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void chat() throws Exception {
        Member member = new Member();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        String message = "asd";
        Long chatRoomId = 1L;

        ChatBotDTO chatBotDTO = new ChatBotDTO();
        chatBotDTO.setId(1L);
        chatBotDTO.setContent("asd");
        when(chatService.detectIntentWithLocation(message, chatRoomId, customUserDetails.getMember())).thenReturn(chatBotDTO);

        mvc.perform(post("/api/chat")
                        .param("message",message)
                        .param("chatRoomId", String.valueOf(chatRoomId))
                        .contentType("application/json")
                        .with(user(customUserDetails))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(chatBotDTO)));

        verify(chatService).detectIntentWithLocation(message, chatRoomId, customUserDetails.getMember());
    }

    @Test
    @DisplayName("채팅 Get - 실패 (잘못된 chatRoomId)")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getChat_InvalidChatRoomId() throws Exception {
        mvc.perform(get("/api/chat")
                        .param("chatRoomId", "invalid_id")
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("채팅 Get - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getChat() throws Exception {
        ChatBotDTO chatBotDTO = new ChatBotDTO();
        chatBotDTO.setId(1L);
        chatBotDTO.setContent("asd");

        UserChatDTO userChatDTO = new UserChatDTO();
        userChatDTO.setId(1L);
        userChatDTO.setContent("asd");
        userChatDTO.setChatDate(new Timestamp(new Date().getTime()));

        ArrayList<UserChatDTO> userChatList = new ArrayList<>();
        userChatList.add(userChatDTO);
        ArrayList<ChatBotDTO> chatBotList = new ArrayList<>();
        chatBotList.add(chatBotDTO);

        ResponseChatDTO responseChatDTO = new ResponseChatDTO();
        responseChatDTO.setChatBot(chatBotList);
        responseChatDTO.setUserChat(userChatList);

        when(chatService.getChat(1L)).thenReturn(responseChatDTO);

        mvc.perform(get("/api/chat")
                        .param("chatRoomId", String.valueOf(1L))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(1L))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseChatDTO)));

        verify(chatService).getChat(1L);
    }
}