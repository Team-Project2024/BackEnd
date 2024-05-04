package Hoseo.GraduationProject.Domain;

import Hoseo.GraduationProject.Admin.Lecture.Domain.Lecture;
import Hoseo.GraduationProject.Member.Domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "course_details")
@NoArgsConstructor
public class CourseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column(name = "grade")
    private String grade;

    @Builder
    CourseDetails(Long id, Member member, Lecture lecture, String grade) {
        this.id = id;
        this.member = member;
        this.lecture = lecture;
        this.grade = grade;
    }
}
