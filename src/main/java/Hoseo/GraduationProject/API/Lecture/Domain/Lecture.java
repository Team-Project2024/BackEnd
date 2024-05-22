package Hoseo.GraduationProject.API.Lecture.Domain;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDTO;
import Hoseo.GraduationProject.Domain.CourseDetails;
import Hoseo.GraduationProject.Member.Domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "lecture")
@NoArgsConstructor
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lecture_name")
    private String lectureName;

    @Column(name = "classification")
    private String classification;

    @Column(name = "room")
    private String room;

    @Column(name = "credit")
    private Long credit;

    @Column(name = "division")
    private Long division;

    @Column(name = "grade")
    private Long grade;

    @Column(name = "lecture_time")
    private String lectureTime;

    @Column(name = "class_method")
    private String classMethod;

    @Column(name = "test_type")
    private String testType;

    @Column(name = "teamwork")
    private Long teamwork;

    @Column(name = "entrepreneurship")
    private Long entrepreneurship;

    @Column(name = "creative_thinking")
    private Long creativeThinking;

    @Column(name = "harnessing_resource")
    private Long harnessingResource;

    @Column(name = "team_play")
    private boolean teamPlay;

    @Column(name = "grade_method")
    private String gradeMethod;

    @Column(name = "ai_sw")
    private boolean aiSw;

    @Column(name = "course_evaluation")
    private Long course_evaluation;

    //교수 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    private List<CourseDetails> courseDetails = new ArrayList<>();

    public void deleteLecture(){
        this.room = null;
    }

    @Builder
    Lecture(Long id, String lectureName, String classification, String room,
            Long credit, Long division, Long grade, String lectureTime, String classMethod,
            String testType, Long teamwork, Long entrepreneurship, Long creativeThinking, Long harnessingResource,
            boolean teamPlay, String gradeMethod, boolean aiSw, Long course_evaluation, Member member) {
        this.id = id;
        this.lectureName = lectureName;
        this.classification = classification;
        this.room = room;
        this.credit = credit;
        this.division = division;
        this.grade = grade;
        this.lectureTime = lectureTime;
        this.classMethod = classMethod;
        this.testType = testType;
        this.teamwork = teamwork;
        this.entrepreneurship = entrepreneurship;
        this.creativeThinking = creativeThinking;
        this.harnessingResource = harnessingResource;
        this.teamPlay = teamPlay;
        this.gradeMethod = gradeMethod;
        this.aiSw = aiSw;
        this.course_evaluation = course_evaluation;
        this.member = member;
    }

    public ResponseLectureDTO toDTO(){
        ResponseLectureDTO responseLectureDTO = new ResponseLectureDTO();
        responseLectureDTO.setLectureId(this.getId());
        responseLectureDTO.setLectureName(this.getLectureName());
        responseLectureDTO.setClassification(this.getClassification());
        responseLectureDTO.setRoom(this.getRoom());
        responseLectureDTO.setCredit(this.getCredit());
        responseLectureDTO.setDivision(this.getDivision());
        responseLectureDTO.setGrade(this.getGrade());
        responseLectureDTO.setLectureTime(this.getLectureTime());
        responseLectureDTO.setClassMethod(this.getClassMethod());
        responseLectureDTO.setTestType(this.getTestType());
        responseLectureDTO.setTeamwork(this.getTeamwork());
        responseLectureDTO.setEntrepreneurship(this.getEntrepreneurship());
        responseLectureDTO.setCreativeThinking(this.getCreativeThinking());
        responseLectureDTO.setHarnessingResource(this.getHarnessingResource());
        responseLectureDTO.setTeamPlay(this.isTeamPlay());
        responseLectureDTO.setGradeMethod(this.getGradeMethod());
        responseLectureDTO.setAiSw(this.isAiSw());
        responseLectureDTO.setCourse_evaluation(this.getCourse_evaluation());
        responseLectureDTO.setMemberId(this.getMember().getId());
        responseLectureDTO.setMemberName(this.getMember().getName());
        responseLectureDTO.setDepartment(this.getMember().getMajor().getDepartment());

        return responseLectureDTO;
    }
}
