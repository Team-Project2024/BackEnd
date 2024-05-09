package Hoseo.GraduationProject.API.Lecture.Service;

import Hoseo.GraduationProject.API.Lecture.DTO.Request.RequestLectureDTO;
import Hoseo.GraduationProject.API.Lecture.DTO.Request.RequestLectureListDTO;
import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.API.Lecture.Domain.Lecture;
import Hoseo.GraduationProject.API.Lecture.ExceptionType.LectureExceptionType;
import Hoseo.GraduationProject.API.Lecture.Repository.LectureRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminLectureService {
    private final LectureRepository lectureRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<ResponseLectureDTO> getLectureList(){
        List<Lecture> lectureList = lectureRepository.findAllWithMemberAndMajor();

        List<ResponseLectureDTO> responseLectureListDTOList = new ArrayList<>();
        for (Lecture lecture : lectureList) {
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

            responseLectureListDTOList.add(responseLectureDTO);
        }

        return responseLectureListDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createLecture(RequestLectureListDTO requestLectureListDTO){
        List<Lecture> lectureList = new ArrayList<>();
        for(RequestLectureDTO requestLectureDTO: requestLectureListDTO.getRequestLectureDTOList()){
            Member member = memberService.getMemberById(requestLectureDTO.getMemberId());

            // 교수만이 강의를 배정 받을 수 있음 때문에 ROLE_PROFESSOR가 아닌 member는 패스
            if(member.getRole().equals("ROLE_PROFESSOR")){
                Lecture lecture = Lecture.builder()
                        .lectureName(requestLectureDTO.getLectureName())
                        .classification(requestLectureDTO.getClassification())
                        .room(requestLectureDTO.getRoom())
                        .credit(requestLectureDTO.getCredit())
                        .division(requestLectureDTO.getDivision())
                        .grade(requestLectureDTO.getGrade())
                        .lectureTime(requestLectureDTO.getLectureTime())
                        .classMethod(requestLectureDTO.getClassMethod())
                        .testType(requestLectureDTO.getTestType())
                        .teamwork(requestLectureDTO.getTeamwork())
                        .entrepreneurship(requestLectureDTO.getEntrepreneurship())
                        .creativeThinking(requestLectureDTO.getCreativeThinking())
                        .harnessingResource(requestLectureDTO.getHarnessingResource())
                        .teamPlay(requestLectureDTO.isTeamPlay())
                        .gradeMethod(requestLectureDTO.getGradeMethod())
                        .aiSw(requestLectureDTO.isAiSw())
                        .course_evaluation(requestLectureDTO.getCourse_evaluation())
                        .member(member)
                        .build();
                lectureList.add(lecture);
            }
        }
        try{
            lectureRepository.saveAll(lectureList);
        } catch(Exception e){
            throw new BusinessLogicException(LectureExceptionType.LECTURE_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteLecture(Long lectureId){
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new BusinessLogicException(LectureExceptionType.LECTURE_NOT_FOUND));
        try{
            lecture.deleteLecture();
            lectureRepository.save(lecture);
        } catch(Exception e){
            throw new BusinessLogicException(LectureExceptionType.LECTURE_SAVE_ERROR);
        }
    }
}
