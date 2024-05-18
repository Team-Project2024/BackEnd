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
            responseLectureListDTOList.add(lecture.toDTO());
        }

        return responseLectureListDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createLecture(RequestLectureListDTO requestLectureListDTO){
        List<Lecture> lectureList = new ArrayList<>();
        for(RequestLectureDTO requestLectureDTO: requestLectureListDTO.getRequestLectureDTOList()){
            Member member = memberService.getMemberById(requestLectureDTO.getMemberId());

            // 교수만이 강의를 배정 받을 수 있기 때문에 ROLE_PROFESSOR가 아닌 member는 패스
            if(member.getRole().equals("ROLE_PROFESSOR")){
                lectureList.add(requestLectureDTO.toEntity(member));
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
        lecture.deleteLecture();
    }
}
