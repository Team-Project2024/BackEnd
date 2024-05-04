package Hoseo.GraduationProject.Admin.Event.Service;

import Hoseo.GraduationProject.Admin.Event.DTO.Request.RequestEventDTO;
import Hoseo.GraduationProject.Admin.Event.DTO.Request.RequestEventListDTO;
import Hoseo.GraduationProject.Admin.Event.DTO.Request.RequestEventUpdateDTO;
import Hoseo.GraduationProject.Admin.Event.DTO.Response.ResponseEventInfoDTO;
import Hoseo.GraduationProject.Admin.Event.Domain.SchoolEvent;
import Hoseo.GraduationProject.Admin.Event.ExceptionType.EventExceptionType;
import Hoseo.GraduationProject.Admin.Event.Repository.SchoolEventRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventService {
    private final SchoolEventRepository schoolEventRepository;

    public List<ResponseEventInfoDTO> getAllEvents() {
        List<SchoolEvent> schoolEvents = schoolEventRepository.findAll();
        List<ResponseEventInfoDTO> responseEventInfoDTOList = new ArrayList<>();
        for (SchoolEvent schoolEvent : schoolEvents) {
            ResponseEventInfoDTO responseEventInfoDTO = new ResponseEventInfoDTO();
            responseEventInfoDTO.setEventId(schoolEvent.getId());
            responseEventInfoDTO.setEventName(schoolEvent.getEventName());
            responseEventInfoDTO.setEventPeriod(schoolEvent.getEventPeriod());
            responseEventInfoDTO.setDescription(schoolEvent.getDescription());
            responseEventInfoDTO.setCancled(schoolEvent.isCancled());
            responseEventInfoDTO.setModified(schoolEvent.isModified());
            responseEventInfoDTOList.add(responseEventInfoDTO);
        }
        return responseEventInfoDTOList;
    }

    public void createEvent(RequestEventListDTO requestEventListDTO){
        List<SchoolEvent> schoolEvents = new ArrayList<>();
        for(RequestEventDTO requestEventDTO : requestEventListDTO.getRequestEventList()){
            SchoolEvent schoolEvent = SchoolEvent.builder()
                    .eventName(requestEventDTO.getEventName())
                    .eventPeriod(requestEventDTO.getEventPeriod())
                    .description(requestEventDTO.getDescription())
                    .modified(false)
                    .isCancled(false)
                    .build();
            schoolEvents.add(schoolEvent);
        }
        try{
            schoolEventRepository.saveAll(schoolEvents);
        } catch(Exception e){
            throw new BusinessLogicException(EventExceptionType.EVENT_SAVE_ERROR);
        }
    }

    public void cancleEvent(Long eventId){
        SchoolEvent schoolEvent = schoolEventRepository.findById(eventId).orElseThrow(
                () -> new BusinessLogicException(EventExceptionType.EVENT_NOT_FOUND));
        schoolEvent.cancleEvent();
        schoolEventRepository.save(schoolEvent);
    }

    public void updateEvent(RequestEventUpdateDTO requestEventUpdateDTO){
        SchoolEvent schoolEvent = schoolEventRepository.findById(requestEventUpdateDTO.getId()).orElseThrow(
                () -> new BusinessLogicException(EventExceptionType.EVENT_NOT_FOUND));
        //날짜 변경 여부 확인
        SchoolEvent newSchoolEvent;
        if(!Objects.equals(schoolEvent.getEventPeriod(), requestEventUpdateDTO.getEventPeriod())){
            newSchoolEvent = SchoolEvent.builder()
                    .id(requestEventUpdateDTO.getId())
                    .eventName(requestEventUpdateDTO.getEventName())
                    .eventPeriod(requestEventUpdateDTO.getEventPeriod())
                    .description(requestEventUpdateDTO.getDescription())
                    .modified(true)
                    .isCancled(schoolEvent.isCancled())
                    .build();
        } else{
            newSchoolEvent = SchoolEvent.builder()
                    .id(requestEventUpdateDTO.getId())
                    .eventName(requestEventUpdateDTO.getEventName())
                    .eventPeriod(requestEventUpdateDTO.getEventPeriod())
                    .description(requestEventUpdateDTO.getDescription())
                    .modified(false)
                    .isCancled(schoolEvent.isCancled())
                    .build();
        }
        schoolEventRepository.save(newSchoolEvent);
    }
}
