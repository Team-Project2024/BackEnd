package Hoseo.GraduationProject.API.Lecture.Controller;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.API.Lecture.Service.StudentLectureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentLectureController.class)
class StudentLectureControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    StudentLectureService studentLectureService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("강의 세부사항 조회 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getLectureInfo() throws Exception {
        ResponseLectureDTO responseLectureDTO = new ResponseLectureDTO();
        responseLectureDTO.setLectureName("asd");
        responseLectureDTO.setClassification("asd");
        responseLectureDTO.setRoom("asd");
        responseLectureDTO.setCredit(1L);
        responseLectureDTO.setDivision(1L);
        responseLectureDTO.setGradeRatio("asd");
        responseLectureDTO.setGrade(1L);
        responseLectureDTO.setLectureTime("asd");
        responseLectureDTO.setClassMethod("asd");
        responseLectureDTO.setTestType("asd");
        responseLectureDTO.setTeamwork(1L);
        responseLectureDTO.setEntrepreneurship(1L);
        responseLectureDTO.setCreativeThinking(1L);
        responseLectureDTO.setHarnessingResource(1L);
        responseLectureDTO.setTeamPlay(true);
        responseLectureDTO.setAiSw(true);
        responseLectureDTO.setGradeMethod("asd");
        responseLectureDTO.setCourse_evaluation(1L);
        responseLectureDTO.setIntroduction("asd");
        responseLectureDTO.setMemberId("201912");

        when(studentLectureService.getLectureInfo(1L)).thenReturn(responseLectureDTO);

        mvc.perform(get("/api/lecture/{lectureId}", 1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseLectureDTO)));
    }
}