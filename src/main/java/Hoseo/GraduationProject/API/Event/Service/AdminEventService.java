package Hoseo.GraduationProject.API.Event.Service;

import Hoseo.GraduationProject.API.Event.DTO.Request.RequestEventDTO;
import Hoseo.GraduationProject.API.Event.DTO.Request.RequestEventListDTO;
import Hoseo.GraduationProject.API.Event.DTO.Request.RequestEventUpdateDTO;
import Hoseo.GraduationProject.API.Event.DTO.Response.ResponseEventInfoDTO;
import Hoseo.GraduationProject.API.Event.Domain.SchoolEvent;
import Hoseo.GraduationProject.API.Event.ExceptionType.EventExceptionType;
import Hoseo.GraduationProject.API.Event.Repository.SchoolEventRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final SchoolEventRepository schoolEventRepository;

    @Transactional(readOnly = true)
    public List<ResponseEventInfoDTO> getAllEvents() {
        List<SchoolEvent> schoolEvents = schoolEventRepository.findAll();
        List<ResponseEventInfoDTO> responseEventInfoDTOList = new ArrayList<>();
        for (SchoolEvent schoolEvent : schoolEvents) {
            ResponseEventInfoDTO responseEventInfoDTO = new ResponseEventInfoDTO();
            responseEventInfoDTO.setEventId(schoolEvent.getId());
            responseEventInfoDTO.setEventName(schoolEvent.getEventName());
            responseEventInfoDTO.setEventPeriod(schoolEvent.getEventPeriod());
            responseEventInfoDTO.setDescription(schoolEvent.getDescription());
            responseEventInfoDTO.setCanceld(schoolEvent.isCanceled());
            responseEventInfoDTO.setModified(schoolEvent.isModified());
            responseEventInfoDTOList.add(responseEventInfoDTO);
        }
        return responseEventInfoDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createEvent(RequestEventListDTO requestEventListDTO){
        List<SchoolEvent> schoolEvents = new ArrayList<>();
        for(RequestEventDTO requestEventDTO : requestEventListDTO.getRequestEventList()){
            SchoolEvent schoolEvent = SchoolEvent.builder()
                    .eventName(requestEventDTO.getEventName())
                    .eventPeriod(requestEventDTO.getEventPeriod())
                    .description(requestEventDTO.getDescription())
                    .modified(false)
                    .isCanceled(false)
                    .build();
            schoolEvents.add(schoolEvent);
        }
        try{
            schoolEventRepository.saveAll(schoolEvents);
        } catch(Exception e){
            throw new BusinessLogicException(EventExceptionType.EVENT_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelEvent(Long eventId){
        SchoolEvent schoolEvent = schoolEventRepository.findById(eventId).orElseThrow(
                () -> new BusinessLogicException(EventExceptionType.EVENT_NOT_FOUND));
        schoolEvent.cancelEvent();

        try{
            schoolEventRepository.save(schoolEvent);
        } catch (Exception e){
            throw new BusinessLogicException(EventExceptionType.EVENT_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateEvent(RequestEventUpdateDTO requestEventUpdateDTO){
        SchoolEvent schoolEvent = schoolEventRepository.findById(requestEventUpdateDTO.getEventId()).orElseThrow(
                () -> new BusinessLogicException(EventExceptionType.EVENT_NOT_FOUND));
        System.out.println(schoolEvent.getId());

        SchoolEvent newSchoolEvent = SchoolEvent.builder()
                .id(requestEventUpdateDTO.getEventId())
                .eventName(requestEventUpdateDTO.getEventName())
                .eventPeriod(requestEventUpdateDTO.getEventPeriod())
                .description(requestEventUpdateDTO.getDescription())
                .isCanceled(schoolEvent.isCanceled())
                .build();

        //날짜 변경 여부 확인
        if(!Objects.equals(schoolEvent.getEventPeriod(), requestEventUpdateDTO.getEventPeriod())){
            newSchoolEvent.modifiedEvent();
        }

        try{
            schoolEventRepository.save(newSchoolEvent);
        } catch (Exception e){
            System.out.println(schoolEvent.getId());
            throw new BusinessLogicException(EventExceptionType.EVENT_SAVE_ERROR);
        }
    }
}
