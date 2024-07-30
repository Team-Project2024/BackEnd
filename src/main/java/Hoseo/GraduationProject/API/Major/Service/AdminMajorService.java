package Hoseo.GraduationProject.API.Major.Service;

import Hoseo.GraduationProject.API.Major.DTO.Page.PageResponse;
import Hoseo.GraduationProject.API.Major.DTO.Request.RequestMajorDTO;
import Hoseo.GraduationProject.API.Major.DTO.Request.RequestMajorListDTO;
import Hoseo.GraduationProject.API.Major.DTO.Response.ResponseListMajorDTO;
import Hoseo.GraduationProject.API.Major.DTO.Response.ResponseMajorDTO;
import Hoseo.GraduationProject.API.Major.Domain.Major;
import Hoseo.GraduationProject.API.Major.ExceptionType.MajorExceptionType;
import Hoseo.GraduationProject.API.Major.Repository.MajorRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMajorService {
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
    @Transactional(readOnly = true)
    public PageResponse getMajorListPaging(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Major> majorPage;

        if (keyword == null || keyword.isEmpty()) {
            majorPage = majorRepository.findAll(pageable);
        } else {
            majorPage = majorRepository.findByKeywordContaining(keyword, pageable);
        }

        List<ResponseMajorDTO> responseMajorDTOS = majorPage.getContent().stream()
                .map(major -> {
                    ResponseMajorDTO responseMajorDTO = new ResponseMajorDTO();
                    responseMajorDTO.setMajorId(major.getMajorId());
                    responseMajorDTO.setDepartment(major.getDepartment());
                    responseMajorDTO.setTrack(major.getTrack());
                    return responseMajorDTO;
                })
                .collect(Collectors.toList());

        return PageResponse.builder()
                .content(responseMajorDTOS)
                .pageNo(majorPage.getNumber())
                .pageSize(majorPage.getSize())
                .totalElements(majorPage.getTotalElements())
                .totalPages(majorPage.getTotalPages())
                .last(majorPage.isLast())
                .build();
    }

    public ResponseListMajorDTO getMajorList(){
        List<Major> majors = majorRepository.findAll();

        ResponseListMajorDTO responseListMajorDTO = new ResponseListMajorDTO();
        List<ResponseMajorDTO> responseMajorDTOS = new ArrayList<>();

        for(Major major: majors){
            ResponseMajorDTO responseMajorDTO = new ResponseMajorDTO();
            responseMajorDTO.setMajorId(major.getMajorId());
            responseMajorDTO.setDepartment(major.getDepartment());
            responseMajorDTO.setTrack(major.getTrack());

            responseMajorDTOS.add(responseMajorDTO);
        }

        responseListMajorDTO.setResponseMajorDTOList(responseMajorDTOS);
        return responseListMajorDTO;
    }
}
