package Hoseo.GraduationProject.Admin.GraduationRequirements.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestGRDTO {
    // 학번
    @NotBlank
    private String year;

    // 인성교양
    @NotNull
    private Long characterCulture;

    // 기초교양
    @NotNull
    private Long basicLiberalArts;

    // 일반교양
    @NotNull
    private Long generalLiberalArts;

    @NotNull
    private Long msc;

    // 전공공통
    @NotNull
    private Long majorCommon;

    // 전공 심화
    @NotNull
    private Long majorAdvanced;

    // 자유선택
    @NotNull
    private Long freeChoice;

    // 졸업학점
    @NotNull
    private Long graduationCredits;

    // 봉사
    @NotNull
    private Long volunteer;

    // 채플
    @NotNull
    private Long chaple;

    // 전공 ID
    @NotNull
    private Long majorId;
}
