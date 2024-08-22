package Hoseo.GraduationProject.Member.Controller;

import Hoseo.GraduationProject.Member.DTO.*;
import Hoseo.GraduationProject.Member.DTO.Response.ResponseProfessorDTO;
import Hoseo.GraduationProject.Member.DTO.Response.ResponseProfessorListDTO;
import Hoseo.GraduationProject.Member.Service.MailSenderService;
import Hoseo.GraduationProject.Member.Service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MemberService memberService;
    @MockBean
    MailSenderService mailSenderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("가입 - 유효성 검사 실패")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void joinProcess_validationFail() throws Exception {
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setId("12"); // 유효성 검사를 통과하지 못함 (6자리 또는 8자리여야 함)
        joinDTO.setPassword("123"); // 유효성 검사를 통과하지 못함 (8자리 이상이어야 함)
        joinDTO.setEmail("invalid-email"); // 유효하지 않은 이메일 형식
        joinDTO.setName("Kim");
        joinDTO.setMajorId(1L);
        joinDTO.setRole(Role.STUDENT);

        mvc.perform(post("/join")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(joinDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest()); // 400 Bad Request

        // 서비스 메서드가 호출되지 않았는지 확인
        verify(memberService, never()).joinProcess(any(JoinDTO.class));
    }

    @Test
    @DisplayName("가입 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void joinProcess() throws Exception {
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setId("20193176");
        joinDTO.setPassword("tjswls12!!");
        joinDTO.setEmail("gwangjeg44@gmail.com");
        joinDTO.setName("김광제");
        joinDTO.setMajorId(1L);
        joinDTO.setRole(Role.STUDENT);

        // Mock 서비스 메서드 호출
        doNothing().when(memberService).joinProcess(any(JoinDTO.class));

        mvc.perform(post("/join")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(joinDTO))
                        .with(csrf()))
                .andExpect(status().isCreated());

        // 서비스 메서드가 호출되었는지 확인
        verify(memberService).joinProcess(any(JoinDTO.class));
    }

    @Test
    @DisplayName("아이디 찾기 - 유효성 검증 실패")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void findId_Validation() throws Exception {
        FindUserIdDTO findUserIdDTO = new FindUserIdDTO();
        findUserIdDTO.setEmail("Invalid-Email"); // 유효하지 않은 이메일 형식
        findUserIdDTO.setName("김광제");

        mvc.perform(post("/find-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(findUserIdDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        // 이 경우에는 서비스 메서드가 호출되지 않아야 함
        verify(memberService, never()).findId(any(FindUserIdDTO.class));
    }


    @Test
    @DisplayName("아이디 찾기 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void findId() throws Exception {
        FindUserIdDTO findUserIdDTO = new FindUserIdDTO();
        findUserIdDTO.setEmail("gwangjeg14@gmail.com");
        findUserIdDTO.setName("김광제");

        // 예상되는 사용자 ID
        String expectedUserId = "user123";

        // findId 메서드가 호출되었을 때 예상되는 ID를 반환하도록 설정
        when(memberService.findId(any(FindUserIdDTO.class))).thenReturn(expectedUserId);

        mvc.perform(post("/find-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(findUserIdDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUserId)); // 반환된 값이 예상된 사용자 ID와 일치하는지 확인

        // 서비스 메서드가 올바르게 호출되었는지 확인
        verify(memberService).findId(any(FindUserIdDTO.class));
    }

    @Test
    @DisplayName("비밀번호 찾기 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void findPw() throws Exception{
        FindUserPWDTO findUserPWDTO = new FindUserPWDTO();
        findUserPWDTO.setEmail("gwangjeg14@gmail.com");
        findUserPWDTO.setName("김광제");
        findUserPWDTO.setId("20193176");

        doNothing().when(memberService).findUser(any(FindUserPWDTO.class));
        doNothing().when(mailSenderService).findPw(any(FindUserPWDTO.class));

        mvc.perform(post("/find-pw")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(findUserPWDTO))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(memberService).findUser(any(FindUserPWDTO.class));
        verify(mailSenderService).findPw(any(FindUserPWDTO.class));
    }

    @Test
    @DisplayName("비밀번호 찾기 - 실패")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void findPw_Invalid() throws Exception {
        FindUserPWDTO findUserPWDTO = new FindUserPWDTO();
        findUserPWDTO.setEmail("Invalid-Email"); // 유효하지 않은 이메일
        findUserPWDTO.setName("김광제");
        findUserPWDTO.setId("20193176");

        mvc.perform(post("/find-pw")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(findUserPWDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest()); // 유효성 검증 실패 시 400 Bad Request 예상

        // 유효성 검증 실패로 인해 서비스 메서드가 호출되지 않아야 함
        verify(memberService, never()).findUser(any(FindUserPWDTO.class));
        verify(mailSenderService, never()).findPw(any(FindUserPWDTO.class));
    }

    @Test
    @DisplayName("메일로 보낸 코드 검증 - 실패")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void codeVerification_Invalid() throws Exception {
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setCode("123456");
        verificationCodeDTO.setId("176");

        mvc.perform(post("/code-verification")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(verificationCodeDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest()); // 유효성 검증 실패 시 400 Bad Request
    }

    @Test
    @DisplayName("메일로 보낸 코드 검증 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void codeVerification() throws Exception{
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setCode("123456");
        verificationCodeDTO.setId("20193176");

        doNothing().when(memberService).codeVerification(any(VerificationCodeDTO.class));

        mvc.perform(post("/code-verification")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(verificationCodeDTO))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 - 실패")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void changePassword_Invalid() throws Exception {
        ChangePwDTO changePwDTO = new ChangePwDTO();
        changePwDTO.setId("2019");
        changePwDTO.setPassword("asdqwe12@@");
        changePwDTO.setCheckPw("asdqwe12@@");

        doNothing().when(memberService).changePassword(any(ChangePwDTO.class));

        mvc.perform(post("/change-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(changePwDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 변경 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void changePassword() throws Exception {
        ChangePwDTO changePwDTO = new ChangePwDTO();
        changePwDTO.setId("20193176");
        changePwDTO.setPassword("asdqwe12@@");
        changePwDTO.setCheckPw("asdqwe12@@");

        doNothing().when(memberService).changePassword(any(ChangePwDTO.class));

        mvc.perform(post("/change-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(changePwDTO))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("교수 정보 조회 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getProfessorList() throws Exception {
        ResponseProfessorListDTO professorListDTO = new ResponseProfessorListDTO();
        ArrayList<ResponseProfessorDTO> list = new ArrayList<>();
        ResponseProfessorDTO professorDTO = new ResponseProfessorDTO();
        professorDTO.setId("123123");
        professorDTO.setName("김광제");
        professorDTO.setDepartment("컴퓨터 공학부");
        list.add(professorDTO);
        professorListDTO.setProfessorDTOList(list);

        when(memberService.getProfessorList()).thenReturn(professorListDTO);

        mvc.perform(get("/admin/member-professor")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(null))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(professorListDTO)));

        // 서비스 메서드가 올바르게 호출되었는지 확인
        verify(memberService).getProfessorList();
    }
}
