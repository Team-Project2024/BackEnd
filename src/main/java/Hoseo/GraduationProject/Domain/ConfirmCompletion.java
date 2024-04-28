package Hoseo.GraduationProject.Domain;

import Hoseo.GraduationProject.Member.Domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "confirm_completion")
@NoArgsConstructor
public class ConfirmCompletion {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", unique = true)
    private Member member;

    @Column(name = "character_culture")
    private Long characterCulture;

    @Column(name = "basic_liberal_arts")
    private Long basicLiberalArts;

    @Column(name = "general_liberal_arts")
    private Long generalLiberalArts;

    @Column(name = "major_common")
    private Long majorCommon;

    @Column(name = "major_advanced")
    private Long majorAdvanced;

    @Column(name = "free_choice")
    private Long freeChoice;

    @Column(name = "graduation_credits")
    private Long graduationCredits;

    @Builder
    ConfirmCompletion(Member member, Long characterCulture, Long basicLiberalArts,
    Long generalLiberalArts, Long majorCommon, Long majorAdvanced, Long freeChoice, Long graduationCredits){
        this.member = member;
        this.characterCulture = characterCulture;
        this.basicLiberalArts = basicLiberalArts;
        this.generalLiberalArts = generalLiberalArts;
        this.majorCommon = majorCommon;
        this.majorAdvanced = majorAdvanced;
        this.freeChoice = freeChoice;
        this.graduationCredits = graduationCredits;
    }
}
