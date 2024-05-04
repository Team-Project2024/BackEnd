package Hoseo.GraduationProject.Admin.Major.Service;

import Hoseo.GraduationProject.Admin.Major.DTO.RequestMajorDTO;
import Hoseo.GraduationProject.Admin.Major.DTO.RequestMajorListDTO;
import Hoseo.GraduationProject.Admin.Major.Domain.Major;
import Hoseo.GraduationProject.Admin.Major.ExceptionType.MajorExceptionType;
import Hoseo.GraduationProject.Admin.Major.Repository.MajorRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;

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
}
