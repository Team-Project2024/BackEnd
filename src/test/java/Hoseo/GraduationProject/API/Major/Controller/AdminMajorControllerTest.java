package Hoseo.GraduationProject.API.Major.Controller;

import Hoseo.GraduationProject.API.Major.DTO.Page.PageResponse;
import Hoseo.GraduationProject.API.Major.DTO.Request.RequestMajorDTO;
import Hoseo.GraduationProject.API.Major.DTO.Request.RequestMajorListDTO;
import Hoseo.GraduationProject.API.Major.DTO.Response.ResponseListMajorDTO;
import Hoseo.GraduationProject.API.Major.DTO.Response.ResponseMajorDTO;
import Hoseo.GraduationProject.API.Major.Service.AdminMajorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdminMajorController.class)
class AdminMajorControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AdminMajorService adminMajorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("전공 추가 - 실패 (유효하지 않은 요청 데이터)")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void createMajorInvalidRequest() throws Exception {
        RequestMajorListDTO requestMajorListDTO = new RequestMajorListDTO();
        RequestMajorDTO requestMajorDTO = new RequestMajorDTO();
        requestMajorDTO.setDepartment("");  // 유효하지 않은 데이터
        requestMajorDTO.setTrack("인공지능");
        ArrayList<RequestMajorDTO> list = new ArrayList<>();
        list.add(requestMajorDTO);
        requestMajorListDTO.setRequestMajorList(list);

        mvc.perform(post("/admin/major")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestMajorListDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest());  // 일반적으로 유효하지 않은 데이터에 대한 응답은 400 Bad Request입니다.
    }


    @Test
    @DisplayName("전공 추가 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void createMajor() throws Exception {
        RequestMajorListDTO requestMajorListDTO = new RequestMajorListDTO();
        RequestMajorDTO requestMajorDTO = new RequestMajorDTO();
        requestMajorDTO.setDepartment("컴퓨터 공학부");
        requestMajorDTO.setTrack("인공지능");
        ArrayList<RequestMajorDTO> list = new ArrayList<>();
        list.add(requestMajorDTO);
        requestMajorListDTO.setRequestMajorList(list);

        doNothing().when(adminMajorService).createMajor(any(RequestMajorListDTO.class));

        mvc.perform(post("/admin/major")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(requestMajorListDTO))
                    .with(csrf()))
                .andExpect(status().isCreated());

        verify(adminMajorService).createMajor(any(RequestMajorListDTO.class));
    }

    @Test
    @DisplayName("전공 리스트 조회 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void testMajorList() throws Exception {
        ResponseListMajorDTO responseListMajorDTO = new ResponseListMajorDTO();
        ResponseMajorDTO responseMajorDTO = new ResponseMajorDTO();
        responseMajorDTO.setMajorId(1L);
        responseMajorDTO.setDepartment("컴퓨터 공학부");
        responseMajorDTO.setTrack("인공지능");
        ArrayList<ResponseMajorDTO> list = new ArrayList<>();
        list.add(responseMajorDTO);
        responseListMajorDTO.setResponseMajorDTOList(list);

        when(adminMajorService.getMajorList()).thenReturn(responseListMajorDTO);

        mvc.perform(get("/admin/major/all")
                    .contentType("application/json")
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseListMajorDTO)));

        verify(adminMajorService, times(2)).getMajorList();
    }
}