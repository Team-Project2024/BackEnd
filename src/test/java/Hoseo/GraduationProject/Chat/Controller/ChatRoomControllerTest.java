package Hoseo.GraduationProject.Chat.Controller;

import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatRoomDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseListChatRoomDTO;
import Hoseo.GraduationProject.Chat.Service.ChatRoomService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ChatRoomService chatRoomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("채팅방 생성 - 실패")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void createChatRoom_Fail() throws Exception {
        Member member = new Member();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        String message = "";

        mvc.perform(post("/api/chat-room")
                        .contentType("application/json")
                        .param("message", message)
                        .with(user(customUserDetails))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(chatRoomService, never()).createChatRoom(customUserDetails.getMember(), message);
    }

    @Test
    @DisplayName("채팅방 생성 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void createChatRoom() throws Exception {
        Member member = new Member();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        String message = "asd";

        when(chatRoomService.createChatRoom(customUserDetails.getMember(),message)).thenReturn(1L);

        mvc.perform(post("/api/chat-room")
                .contentType("application/json")
                .param("message", message)
                .with(user(customUserDetails))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(1L)));

        verify(chatRoomService).createChatRoom(customUserDetails.getMember(), message);
    }

    @Test
    @DisplayName("채팅방 리스트 Get - NoContent")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getChatRoomList_NoContent() throws Exception {
        ResponseListChatRoomDTO responseListChatRoomDTO = new ResponseListChatRoomDTO();
        ArrayList<ResponseChatRoomDTO> list = new ArrayList<>();
        responseListChatRoomDTO.setRepsonseChatRoomDTOList(list);

        Member member = new Member();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        when(chatRoomService.getChatRoomList(customUserDetails.getMember())).thenReturn(responseListChatRoomDTO);

        mvc.perform(get("/api/chat-room")
                        .contentType("application/json")
                        .with(user(customUserDetails))
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().json(objectMapper.writeValueAsString(responseListChatRoomDTO)));

        verify(chatRoomService).getChatRoomList(customUserDetails.getMember());
    }

    @Test
    @DisplayName("채팅방 리스트 Get - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getChatRoomList() throws Exception {
        ResponseListChatRoomDTO responseListChatRoomDTO = new ResponseListChatRoomDTO();
        ResponseChatRoomDTO responseChatRoomDTO = new ResponseChatRoomDTO();
        responseChatRoomDTO.setFirstChat("asd");
        responseChatRoomDTO.setChatRoomId(1L);
        responseChatRoomDTO.setLastChatDate(new Timestamp(new Date().getTime()));
        ArrayList<ResponseChatRoomDTO> list = new ArrayList<>();
        list.add(responseChatRoomDTO);
        responseListChatRoomDTO.setRepsonseChatRoomDTOList(list);

        Member member = new Member();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        when(chatRoomService.getChatRoomList(customUserDetails.getMember())).thenReturn(responseListChatRoomDTO);

        mvc.perform(get("/api/chat-room")
                    .contentType("application/json")
                    .with(user(customUserDetails))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseListChatRoomDTO)));

        verify(chatRoomService).getChatRoomList(customUserDetails.getMember());
    }

    @Test
    @DisplayName("단일 채팅방 삭제 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void deleteChatRoom() throws Exception {
        Member member = new Member();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        doNothing().when(chatRoomService).deleteChatRoom(customUserDetails.getMember(),1L);

        mvc.perform(delete("/api/chat-room")
                        .param("chatRoomId",String.valueOf(1L))
                    .contentType("application/json")
                    .with(user(customUserDetails))
                    .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("전체 채팅방 삭제 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void deleteAllChatRoom() throws Exception {
        Member member = new Member();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        doNothing().when(chatRoomService).deleteAllChatRoom(customUserDetails.getMember());

        mvc.perform(delete("/api/all/chat-room")
                    .contentType("application/json")
                    .with(user(customUserDetails))
                    .with(csrf()))
                .andExpect(status().isOk());
    }
}