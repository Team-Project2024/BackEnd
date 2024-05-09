package Hoseo.GraduationProject.API.Lecture.DTO.Request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLectureDTO {
    //강의명
    @NotBlank
    private String lectureName;

    //이수구분
    @NotBlank
    private String classification;

    //강의실
    @NotBlank
    private String room;

    //학점
    @NotNull
    private Long credit;

    //분반
    @NotNull
    private Long division;

    //개설학년
    @NotNull
    private Long grade;

    //강의시간
    @NotBlank
    private String lectureTime;

    //수업방식
    @NotBlank
    private String classMethod;

    //시험유형
    @NotBlank
    private String testType;

    //TECH
    @Nullable
    private Long teamwork;
    @Nullable
    private Long entrepreneurship;
    @Nullable
    private Long creativeThinking;
    @Nullable
    private Long harnessingResource;

    //팀플유무
    @NotNull
    private boolean teamPlay;

    //성적산출방식
    @NotBlank
    private String gradeMethod;

    //AISW디그리
    @NotNull
    private boolean aiSw;

    //강의평가
    @NotNull
    private Long course_evaluation;

    //교수 ID 6자리여야됨
    @NotBlank
    private String memberId;
}
