package Hoseo.GraduationProject.API.SchoolLocation.Service;

import Hoseo.GraduationProject.API.SchoolLocation.DTO.ResponseSchoolLocation;
import Hoseo.GraduationProject.API.SchoolLocation.Domain.SchoolLocation;
import Hoseo.GraduationProject.API.SchoolLocation.Repository.SchoolLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolLocationService {
    private final SchoolLocationRepository schoolLocationRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "schoolLocationCache", key = "#schoolLocationIdList",
            cacheManager = "cacheManager")
    public List<ResponseSchoolLocation> getSchoolLocationList(List<Long> schoolLocationIdList){
        List<SchoolLocation> schoolLocationList = schoolLocationRepository.findAllById(schoolLocationIdList);
        List<ResponseSchoolLocation> responseSchoolLocationList = new ArrayList<>();

        log.info("Fetching data from DB Not Redis Cache");

        for(SchoolLocation schoolLocation : schoolLocationList){
            ResponseSchoolLocation responseSchoolLocation = new ResponseSchoolLocation();
            responseSchoolLocation.setCategory(schoolLocation.getCategory());
            responseSchoolLocation.setLocationName(schoolLocation.getLocationName());
            responseSchoolLocation.setLat(schoolLocation.getLat());
            responseSchoolLocation.setLon(schoolLocation.getLon());

            responseSchoolLocationList.add(responseSchoolLocation);
        }

        return responseSchoolLocationList;
    }
}
