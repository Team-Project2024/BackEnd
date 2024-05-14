package Hoseo.GraduationProject.API.Lecture.Service;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.API.Lecture.Domain.Lecture;
import Hoseo.GraduationProject.API.Lecture.ExceptionType.LectureExceptionType;
import Hoseo.GraduationProject.API.Lecture.Repository.LectureRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentLectureService {
    private final LectureRepository lectureRepository;

    @Transactional(readOnly = true)
    public ResponseLectureDTO getLectureInfo(Long lectureId){
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new BusinessLogicException(LectureExceptionType.LECTURE_NOT_FOUND));

        return lecture.toDTO();
    }

    public List<ResponseLectureDTO> getLectureListDTO(List<Long> lectureIds){
        List<Lecture> lectureList = lectureRepository.findAllById(lectureIds);
        List<ResponseLectureDTO> responseLectureDTOList = new ArrayList<>();
        for(Lecture lecture: lectureList){
            responseLectureDTOList.add(lecture.toDTO());
        }
        return responseLectureDTOList;
    }
}
