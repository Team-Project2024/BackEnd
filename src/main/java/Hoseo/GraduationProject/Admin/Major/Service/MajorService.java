package Hoseo.GraduationProject.Admin.Major.Service;

import Hoseo.GraduationProject.Admin.Major.DTO.Request.RequestMajorDTO;
import Hoseo.GraduationProject.Admin.Major.DTO.Request.RequestMajorListDTO;
import Hoseo.GraduationProject.Admin.Major.DTO.Response.ResponseMajorDTO;
import Hoseo.GraduationProject.Admin.Major.Domain.Major;
import Hoseo.GraduationProject.Admin.Major.ExceptionType.MajorExceptionType;
import Hoseo.GraduationProject.Admin.Major.Repository.MajorRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createMajor(RequestMajorListDTO requestMajorListDTO){
        List<Major> majors = new ArrayList<>();
        for(RequestMajorDTO requestMajorDTO: requestMajorListDTO.getRequestMajorList()){
            Major major = Major.builder()
                    .department(requestMajorDTO.getDepartment())
                    .track(requestMajorDTO.getTrack())
                    .build();
            majors.add(major);
        }
        try{
            majorRepository.saveAll(majors);
        } catch (Exception e) {
            throw new BusinessLogicException(MajorExceptionType.MAJOR_SAVE_ERROR);
        }
    }

    //major ID를 받아서 Major 엔티티를 반환하는 메서드
    public Major getMajor(Long majorId){
        return majorRepository.findById(majorId).orElseThrow(
                () -> new BusinessLogicException(MajorExceptionType.MAJOR_NOT_FOUND));
    }

    //전체 major를 반환하는 메서드 major의 id, 학과, 트랙을 반환
    public List<ResponseMajorDTO> getMajorList(){
        List<Major> majors = majorRepository.findAll();
        List<ResponseMajorDTO> responseMajorDTOS = new ArrayList<>();
        for(Major major: majors){
            ResponseMajorDTO responseMajorDTO = new ResponseMajorDTO();
            responseMajorDTO.setMajorId(major.getMajorId());
            responseMajorDTO.setDepartment(major.getDepartment());
            responseMajorDTO.setTrack(major.getTrack());

            responseMajorDTOS.add(responseMajorDTO);
        }

        return responseMajorDTOS;
    }
}
