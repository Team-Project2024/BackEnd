package Hoseo.GraduationProject.API.GraduationRequirements.Controller;

import Hoseo.GraduationProject.API.GraduationRequirements.DTO.GRDTO;
import Hoseo.GraduationProject.API.GraduationRequirements.Service.StudentGRService;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentGRController.class)
class StudentGRControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    StudentGRService studentGRService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("졸업요건 조회 - 성공")
    void getGR() throws Exception {
        GRDTO grDTO = new GRDTO();
        grDTO.setGraduationCredits(1L);
        grDTO.setMsc(1L);
        grDTO.setGeneralLiberalArts(1L);
        grDTO.setYear("2019");
        grDTO.setCharacterCulture(1L);
        grDTO.setMajorId(1L);
        grDTO.setBasicLiberalArts(1L);
        grDTO.setMajorCommon(1L);
        grDTO.setMajorAdvanced(1L);
        grDTO.setFreeChoice(1L);

        Member member = new Member();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        when(studentGRService.getGR(member.getId())).thenReturn(grDTO);

        mvc.perform(get("/api/graduation")
                        .with(user(customUserDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(grDTO)));

        verify(studentGRService).getGR(member.getId());
    }
}
