package Hoseo.GraduationProject.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

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

//    @OneToMany(mappedBy = "major", cascade = CascadeType.REMOVE)
//    private List<Member> member;

//    @OneToMany(mappedBy = "major", cascade = CascadeType.REMOVE)
//    private List<GraduationRequirements> graduationRequirements;

    @Builder
    Major(Long majorId, String department, String track) {
        this.majorId = majorId;
        this.department = department;
        this.track = track;
    }
}
