package Hoseo.GraduationProject.Admin.GraduationRequirements.Service;

import Hoseo.GraduationProject.Admin.GraduationRequirements.DTO.RequestGRDTO;
import Hoseo.GraduationProject.Admin.GraduationRequirements.DTO.RequestGRListDTO;
import Hoseo.GraduationProject.Admin.GraduationRequirements.Domain.GraduationRequirements;
import Hoseo.GraduationProject.Admin.GraduationRequirements.ExceptionType.GRExceptionType;
import Hoseo.GraduationProject.Admin.GraduationRequirements.Repository.GRRepository;
import Hoseo.GraduationProject.Admin.Major.Service.MajorService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GRService {
    private final GRRepository grRepository;
    private final MajorService majorService;

    private static final Long VOLUNTEER = 30L;
    private static final Long CHAPEL = 4L;

    @Transactional(rollbackFor = Exception.class)
    public void createGR(RequestGRListDTO requestGRListDTO){
        List<GraduationRequirements> graduationRequirementsList = new ArrayList<>();
        for(RequestGRDTO requestGRDTO: requestGRListDTO.getRequestGRList()){
            GraduationRequirements graduationRequirements = GraduationRequirements.builder()
                    .year(requestGRDTO.getYear())
                    .characterCulture(requestGRDTO.getCharacterCulture())
                    .basicLiberalArts(requestGRDTO.getBasicLiberalArts())
                    .generalLiberalArts(requestGRDTO.getGeneralLiberalArts())
                    .msc(requestGRDTO.getMsc())
                    .majorCommon(requestGRDTO.getMajorCommon())
                    .majorAdvanced(requestGRDTO.getMajorAdvanced())
                    .freeChoice(requestGRDTO.getFreeChoice())
                    .graduationCredits(requestGRDTO.getGraduationCredits())
                    .volunteer(VOLUNTEER)
                    .chapel(CHAPEL)
                    .major(majorService.getMajor(requestGRDTO.getMajorId()))
                    .build();
            graduationRequirementsList.add(graduationRequirements);
        }

        try{
            grRepository.saveAll(graduationRequirementsList);
        } catch(Exception e){
            throw new BusinessLogicException(GRExceptionType.GR_SAVE_ERROR);
        }
    }
}
