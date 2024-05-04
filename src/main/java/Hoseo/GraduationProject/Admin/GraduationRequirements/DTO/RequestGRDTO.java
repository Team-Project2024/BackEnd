package Hoseo.GraduationProject.Admin.GraduationRequirements.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestGRDTO {
    // 학번
    private String year;

    // 인성교양
    private Long characterCulture;

    // 기초교양
    private Long basicLiberalArts;

    // 일반교양
    private Long generalLiberalArts;

    // 전공공통
    private Long majorCommon;

    // 전공 심화
    private Long majorAdvanced;

    // 자유선택
    private Long freeChoice;

    // 졸업학점
    private Long graduationCredits;

    // 봉사
    private Long volunteer;

    // 채플
    private Long chaple;

    // 전공 ID
    private Long majorId;
}
