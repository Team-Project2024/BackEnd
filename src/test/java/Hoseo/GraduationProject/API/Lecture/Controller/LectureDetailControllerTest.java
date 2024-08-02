package Hoseo.GraduationProject.API.Lecture.Controller;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDetailDTO;
import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDetailListDTO;
import Hoseo.GraduationProject.API.Lecture.Service.LectureDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LectureDetailController.class)
class LectureDetailControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LectureDetailService lectureDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("강의 세부사항 조회 - 성공")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getLectureDetail() throws Exception {
        ResponseLectureDetailListDTO responseLectureDetailListDTO = new ResponseLectureDetailListDTO();
        ArrayList<ResponseLectureDetailDTO> list = new ArrayList<>();
        ResponseLectureDetailDTO responseLectureDetailDTO = new ResponseLectureDetailDTO();
        responseLectureDetailDTO.setWeek(1L);
        responseLectureDetailDTO.setContent("asd");
        list.add(responseLectureDetailDTO);
        responseLectureDetailListDTO.setResponseLectureDetailDTOS(list);

        Long lectureId = 1L;

        // Setup the expected behavior with limit on invocation count
        when(lectureDetailService.getLectureDetails(lectureId)).thenReturn(responseLectureDetailListDTO);

        mvc.perform(get("/api/lecture/detail/{lectureId}", lectureId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseLectureDetailListDTO)));

        // Verify the service method is called only once
        verify(lectureDetailService, times(2)).getLectureDetails(lectureId);
    }


    @Test
    @DisplayName("강의 세부사항 조회 - 실패 (내용 없음)")
    @WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
    void getLectureDetailNoContent() throws Exception {
        Long lectureId = 1L;
        ResponseLectureDetailListDTO emptyResponse = new ResponseLectureDetailListDTO();
        emptyResponse.setResponseLectureDetailDTOS(new ArrayList<>());

        when(lectureDetailService.getLectureDetails(lectureId)).thenReturn(emptyResponse);

        mvc.perform(get("/api/lecture/detail/{lectureId}", lectureId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(lectureDetailService, times(1)).getLectureDetails(lectureId);
    }

}