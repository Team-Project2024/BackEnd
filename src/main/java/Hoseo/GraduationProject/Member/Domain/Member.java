package Hoseo.GraduationProject.Member.Domain;

import Hoseo.GraduationProject.API.Lecture.Domain.Lecture;
import Hoseo.GraduationProject.API.Major.Domain.Major;
import Hoseo.GraduationProject.Chat.Domain.ChatRoom;
import Hoseo.GraduationProject.Domain.ConfirmCompletion;
import Hoseo.GraduationProject.Domain.CourseDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "id")
    private String id;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Email
    @NotBlank
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @Column(name = "role")
    private String role;

    @Column(name="teamwork")
    private Long teamwork;

    @Column(name="entrepreneurship")
    private Long entrepreneurship;

    @Column(name="creative_thinking")
    private Long creativeThinking;

    @Column(name="harnessing_resource")
    private Long harnessingResource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private ConfirmCompletion confirmCompletion;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Lecture> lectures = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<CourseDetails> courseDetails = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    public void updatePassword(String password){
        this.password = password;
    }

    @Builder
    Member(String id, String password, String name,
           String email, String role, Long teamwork, Long entrepreneurship,
           Long creativeThinking, Long harnessingResource, Major major){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.teamwork = teamwork;
        this.entrepreneurship = entrepreneurship;
        this.creativeThinking = creativeThinking;
        this.harnessingResource = harnessingResource;
        this.major = major;
    }

    @Override
    public String toString(){
        return "Member{id:"+id+"}";
    }
}
