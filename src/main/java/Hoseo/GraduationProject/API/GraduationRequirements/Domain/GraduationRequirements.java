package Hoseo.GraduationProject.API.GraduationRequirements.Domain;

import Hoseo.GraduationProject.API.Major.Domain.Major;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Table(name = "graduation_requirements")
@NoArgsConstructor
public class GraduationRequirements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "year")
    private String year;

    @Column(name = "character_culture")
    private Long characterCulture;

    @Column(name = "basic_liberal_arts")
    private Long basicLiberalArts;

    @Column(name = "general_liberal_arts")
    private Long generalLiberalArts;

    @Column(name = "msc")
    private Long msc;

    @Column(name = "major_common")
    private Long majorCommon;

    @Column(name = "major_advanced")
    private Long majorAdvanced;

    @Column(name = "free_choice")
    private Long freeChoice;

    @Column(name = "graduation_credits")
    private Long graduationCredits;

    @Column(name = "volunteer")
    private Long volunteer;

    @Column(name = "chapel")
    private Long chapel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Builder
    GraduationRequirements(Long id, String year, Long characterCulture,
                           Long basicLiberalArts, Long generalLiberalArts, Long msc,
                           Long majorCommon, Long majorAdvanced, Long freeChoice, Long graduationCredits,
                           Long volunteer, Long chapel, Major major) {
        this.id = id;
        this.year = year;
        this.characterCulture = characterCulture;
        this.basicLiberalArts = basicLiberalArts;
        this.generalLiberalArts = generalLiberalArts;
        this.msc = msc;
        this.majorCommon = majorCommon;
        this.majorAdvanced = majorAdvanced;
        this.freeChoice = freeChoice;
        this.graduationCredits = graduationCredits;
        this.volunteer = volunteer;
        this.chapel = chapel;
        this.major = major;
    }
}
