package Hoseo.GraduationProject.API.Lecture.Controller;

import Hoseo.GraduationProject.API.Lecture.DTO.Page.PageResponse;
import Hoseo.GraduationProject.API.Lecture.DTO.Request.RequestLectureDTO;
import Hoseo.GraduationProject.API.Lecture.DTO.Request.RequestLectureListDTO;
import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.API.Lecture.Service.AdminLectureService;
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
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminLectureController.class)
class AdminLectureControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AdminLectureService adminLectureService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Lecture List 페이징 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getLectureListPage() throws Exception {
        // 페이지 응답 객체 생성 및 설정
        ArrayList<ResponseLectureDTO> list = new ArrayList<>();
        ResponseLectureDTO responseLectureDTO = new ResponseLectureDTO();
        list.add(responseLectureDTO);

        PageResponse pageResponse = PageResponse.builder()
                .content(list)
                .pageNo(0)
                .pageSize(1)
                .totalElements(1L)
                .totalPages(1)
                .last(true)
                .build();

        // adminLectureService의 getLectureListPage 메서드가 올바른 값을 반환하도록 mock 설정
        when(adminLectureService.getLectureListPage(0, 10, "")).thenReturn(pageResponse);

        // mvc.perform을 통해 GET 요청 및 결과 확인
        mvc.perform(get("/admin/lecture")
                        .param("page", "0")
                        .param("keyword", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));

        // adminLectureService의 메서드 호출을 검증
        verify(adminLectureService).getLectureListPage(0, 10, "");
    }

    @Test
    @DisplayName("Lecture List 페이징 - No Content")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getLectureListPageNoContent() throws Exception {
        // 빈 페이지 응답 객체 생성 및 설정
        PageResponse emptyPageResponse = PageResponse.builder()
                .content(Collections.emptyList())
                .pageNo(0)
                .pageSize(1)
                .totalElements(0L)
                .totalPages(0)
                .last(true)
                .build();

        // adminLectureService의 getLectureListPage 메서드가 빈 값을 반환하도록 mock 설정
        when(adminLectureService.getLectureListPage(0, 10, "")).thenReturn(emptyPageResponse);

        // mvc.perform을 통해 GET 요청 및 결과 확인
        mvc.perform(get("/admin/lecture")
                        .param("page", "0")
                        .param("keyword", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().json(objectMapper.writeValueAsString(emptyPageResponse)));

        // adminLectureService의 메서드 호출을 검증
        verify(adminLectureService).getLectureListPage(0, 10, "");
    }


    @Test
    @DisplayName("강의 목록 추가 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void createLecture() throws Exception {
        RequestLectureListDTO requestLectureListDTO = new RequestLectureListDTO();
        
        RequestLectureDTO requestLectureDTO = new RequestLectureDTO();
        requestLectureDTO.setLectureName("asd");
        requestLectureDTO.setClassification("asd");
        requestLectureDTO.setRoom("asd");
        requestLectureDTO.setCredit(1L);
        requestLectureDTO.setDivision(1L);
        requestLectureDTO.setGradeRatio("asd");
        requestLectureDTO.setGrade(1L);
        requestLectureDTO.setLectureTime("asd");
        requestLectureDTO.setClassMethod("asd");
        requestLectureDTO.setTestType("asd");
        requestLectureDTO.setTeamwork(1L);
        requestLectureDTO.setEntrepreneurship(1L);
        requestLectureDTO.setCreativeThinking(1L);
        requestLectureDTO.setHarnessingResource(1L);
        requestLectureDTO.setTeamPlay(true);
        requestLectureDTO.setAiSw(true);
        requestLectureDTO.setGradeMethod("asd");
        requestLectureDTO.setCourse_evaluation(1L);
        requestLectureDTO.setIntroduction("asd");
        requestLectureDTO.setMemberId("201912");
        
        ArrayList<RequestLectureDTO> list = new ArrayList<>();
        list.add(requestLectureDTO);
        
        requestLectureListDTO.setRequestLectureDTOList(list);
        
        doNothing().when(adminLectureService).createLecture(any(RequestLectureListDTO.class));
        
        mvc.perform(post("/admin/lecture")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(requestLectureListDTO))
                    .with(csrf()))
                .andExpect(status().isCreated());
        
        verify(adminLectureService).createLecture(any(RequestLectureListDTO.class));
    }

    @Test
    @DisplayName("강의 삭제 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void deleteLecture() throws Exception {
        Long lectureId = 1L;

        doNothing().when(adminLectureService).deleteLecture(lectureId);

        mvc.perform(put("/admin/lecture/{lectureId}", lectureId)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(adminLectureService).deleteLecture(lectureId);
    }
}