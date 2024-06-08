package Hoseo.GraduationProject.API.GraduationRequirements.Service;

import Hoseo.GraduationProject.API.GraduationRequirements.DTO.GRDTO;
import Hoseo.GraduationProject.API.GraduationRequirements.Domain.GraduationRequirements;
import Hoseo.GraduationProject.API.GraduationRequirements.Repository.GRRepository;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentGRService {
    private final GRRepository grRepository;
    private final MemberService memberService;
    private final int START_INDEX_OF_YEAR = 0;
    private final int END_INDEX_OF_YEAR = 4;

    public GRDTO getGR(String memberId){
        Member member = memberService.getMemberById(memberId);

        System.out.println(member.getMajor().getMajorId());

        GraduationRequirements GR =
                grRepository.getGRByYearAndMajorId
                        (member.getId().substring(START_INDEX_OF_YEAR, END_INDEX_OF_YEAR),
                                member.getMajor().getMajorId());

        GRDTO dto = new GRDTO();
        dto.setYear(GR.getYear());
        dto.setMsc(GR.getMsc());
        dto.setFreeChoice(GR.getFreeChoice());
        dto.setMajorId(GR.getMajor().getMajorId());
        dto.setMajorCommon(GR.getMajorCommon());
        dto.setMajorAdvanced(GR.getMajorAdvanced());
        dto.setCharacterCulture(GR.getCharacterCulture());
        dto.setBasicLiberalArts(GR.getBasicLiberalArts());
        dto.setGeneralLiberalArts(GR.getGeneralLiberalArts());
        dto.setGraduationCredits(GR.getGraduationCredits());
        return dto;
    }
}
