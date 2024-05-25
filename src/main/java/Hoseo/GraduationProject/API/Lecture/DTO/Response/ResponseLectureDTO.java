package Hoseo.GraduationProject.API.Lecture.DTO.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseLectureDTO {

    //강의 ID
    private Long lectureId;

    //강의명
    private String lectureName;

    //이수구분
    private String classification;

    //강의실
    private String room;

    //학점
    private Long credit;

    //분반
    private Long division;

    //개설학년
    private Long grade;

    //강의시간
    private String lectureTime;

    //수업방식
    private String classMethod;

    //시험유형
    private String testType;

    //TECH
    private Long teamwork;
    private Long entrepreneurship;
    private Long creativeThinking;
    private Long harnessingResource;

    //팀플유무
    private boolean teamPlay;

    //성적산출방식
    private String gradeMethod;

    //AISW디그리
    private boolean aiSw;

    //강의평가
    private Long course_evaluation;

    //교수 ID 6자리여야됨
    private String memberId;

    //교수명
    private String memberName;

    //교수 학과
    private String department;

    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
