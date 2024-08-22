package Hoseo.GraduationProject.API.GraduationRequirements.Controller;

import Hoseo.GraduationProject.API.GraduationRequirements.DTO.GRDTO;
import Hoseo.GraduationProject.API.GraduationRequirements.DTO.GRListDTO;
import Hoseo.GraduationProject.API.GraduationRequirements.Service.AdminGRService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminGRController.class)
class AdminGRControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AdminGRService adminGRService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("졸업 요건 추가 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void createGR() throws Exception {
        ArrayList<GRDTO> list = new ArrayList<>();

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
        list.add(grDTO);

        GRListDTO gr = new GRListDTO();
        gr.setRequestGRList(list);

        // Mock 서비스 메서드 호출
        doNothing().when(adminGRService).createGR(any(GRListDTO.class));

        mvc.perform(post("/admin/graduation")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(gr))
                        .with(csrf()))
                .andExpect(status().isCreated());

        // 서비스 메서드가 호출되었는지 확인
        verify(adminGRService).createGR(any(GRListDTO.class));
    }
}