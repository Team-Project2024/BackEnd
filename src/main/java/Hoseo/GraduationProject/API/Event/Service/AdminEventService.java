package Hoseo.GraduationProject.API.Event.Service;

import Hoseo.GraduationProject.API.Event.DTO.Request.RequestEventDTO;
import Hoseo.GraduationProject.API.Event.DTO.Request.RequestEventListDTO;
import Hoseo.GraduationProject.API.Event.DTO.Request.RequestEventUpdateDTO;
import Hoseo.GraduationProject.API.Event.DTO.Response.ResponseEventInfoDTO;
import Hoseo.GraduationProject.API.Event.DTO.Response.ResponseListEventInfoDTO;
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
    public ResponseListEventInfoDTO getAllEvents() {
        ResponseListEventInfoDTO responseListEventInfoDTO = new ResponseListEventInfoDTO();
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
        responseListEventInfoDTO.setResponseEventInfoDTOList(responseEventInfoDTOList);
        return responseListEventInfoDTO;
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
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateEvent(Long eventId, RequestEventUpdateDTO requestEventUpdateDTO){
        SchoolEvent schoolEvent = schoolEventRepository.findById(eventId).orElseThrow(
                () -> new BusinessLogicException(EventExceptionType.EVENT_NOT_FOUND));

        //날짜 변경 여부 확인
        if(!Objects.equals(schoolEvent.getEventPeriod(), requestEventUpdateDTO.getEventPeriod())){
            schoolEvent.modifiedEvent();
        }

        schoolEvent.updateEventName(requestEventUpdateDTO.getEventName());
        schoolEvent.updateEventPeriod(requestEventUpdateDTO.getEventPeriod());
        schoolEvent.updateDescription(requestEventUpdateDTO.getDescription());
    }
}
