package Hoseo.GraduationProject.API.Lecture.Service;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDetailDTO;
import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDetailListDTO;
import Hoseo.GraduationProject.API.Lecture.Domain.LectureDetail;
import Hoseo.GraduationProject.API.Lecture.Repository.LectureDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureDetailService {
    private final LectureDetailRepository lectureDetailRepository;

    @Transactional(readOnly = true)
    public ResponseLectureDetailListDTO getLectureDetails(Long lectureId){
        List<LectureDetail> lectureDetailList = lectureDetailRepository.findAllByLectureId(lectureId);
        ResponseLectureDetailListDTO responseLectureDetailListDTO = new ResponseLectureDetailListDTO();
        List<ResponseLectureDetailDTO> responseLectureDetailDTOList = new ArrayList<>();

        for(LectureDetail lectureDetail : lectureDetailList){
            ResponseLectureDetailDTO responseLectureDetailDTO = new ResponseLectureDetailDTO();
            responseLectureDetailDTO.setWeek(lectureDetail.getWeek());
            responseLectureDetailDTO.setContent(lectureDetail.getContent());
            responseLectureDetailDTOList.add(responseLectureDetailDTO);
        }

        responseLectureDetailListDTO.setResponseLectureDetailDTOS(responseLectureDetailDTOList);
        return responseLectureDetailListDTO;
    }
}
