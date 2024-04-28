package Hoseo.GraduationProject.Domain;

import Hoseo.GraduationProject.Member.Domain.Member;
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
    @Column(name = "college")
    private String college;

    @NotBlank
    @Column(name = "department")
    private String department;

    @Null
    @Column(name = "track")
    private String track;

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL)
    private List<Member> member;

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL)
    private List<GraduationRequirements> graduationRequirements;

    @Builder
    Major(Long majorId, String college,
          String department, String track) {
        this.majorId = majorId;
        this.college = college;
        this.department = department;
        this.track = track;
    }
}
