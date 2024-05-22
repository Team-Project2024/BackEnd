package Hoseo.GraduationProject.API.Major.Domain;

import Hoseo.GraduationProject.API.GraduationRequirements.Domain.GraduationRequirements;
import Hoseo.GraduationProject.Member.Domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.List;

@Entity
@Getter
@Table(name = "major")
@NoArgsConstructor
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Long majorId;

    @NotBlank
    @Column(name = "department")
    private String department;

    @Null
    @Column(name = "track")
    private String track;

    @JsonIgnore
    @OneToMany(mappedBy = "major")
    private List<Member> member;

    @JsonIgnore
    @OneToMany(mappedBy = "major")
    private List<GraduationRequirements> graduationRequirements;

    @Builder
    Major(Long majorId, String department, String track) {
        this.majorId = majorId;
        this.department = department;
        this.track = track;
    }
}
