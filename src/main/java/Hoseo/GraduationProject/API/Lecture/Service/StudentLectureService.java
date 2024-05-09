package Hoseo.GraduationProject.API.Lecture.Service;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.API.Lecture.Domain.Lecture;
import Hoseo.GraduationProject.API.Lecture.ExceptionType.LectureExceptionType;
import Hoseo.GraduationProject.API.Lecture.Repository.LectureRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentLectureService {
    private final LectureRepository lectureRepository;

    @Transactional(readOnly = true)
    public ResponseLectureDTO getLectureInfo(Long lectureId){
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new BusinessLogicException(LectureExceptionType.LECTURE_NOT_FOUND));

        ResponseLectureDTO responseLectureDTO = new ResponseLectureDTO();
        responseLectureDTO.setLectureId(lecture.getId());
        responseLectureDTO.setLectureName(lecture.getLectureName());
        responseLectureDTO.setClassification(lecture.getClassification());
        responseLectureDTO.setRoom(lecture.getRoom());
        responseLectureDTO.setCredit(lecture.getCredit());
        responseLectureDTO.setDivision(lecture.getDivision());
        responseLectureDTO.setGrade(lecture.getGrade());
        responseLectureDTO.setLectureTime(lecture.getLectureTime());
        responseLectureDTO.setClassMethod(lecture.getClassMethod());
        responseLectureDTO.setTestType(lecture.getTestType());
        responseLectureDTO.setTeamwork(lecture.getTeamwork());
        responseLectureDTO.setEntrepreneurship(lecture.getEntrepreneurship());
        responseLectureDTO.setCreativeThinking(lecture.getCreativeThinking());
        responseLectureDTO.setHarnessingResource(lecture.getHarnessingResource());
        responseLectureDTO.setTeamPlay(lecture.isTeamPlay());
        responseLectureDTO.setGradeMethod(lecture.getGradeMethod());
        responseLectureDTO.setAiSw(lecture.isAiSw());
        responseLectureDTO.setCourse_evaluation(lecture.getCourse_evaluation());
        responseLectureDTO.setMemberId(lecture.getMember().getId());
        responseLectureDTO.setMemberName(lecture.getMember().getName());
        responseLectureDTO.setDepartment(lecture.getMember().getMajor().getDepartment());

        return responseLectureDTO;
    }
}
