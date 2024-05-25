package Hoseo.GraduationProject.API.Event.Service;

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

@Service
@RequiredArgsConstructor
public class StudentEventService {
    private final SchoolEventRepository schoolEventRepository;

    @Transactional(readOnly = true)
    public ResponseEventInfoDTO getEventInfo(Long eventId) {
        SchoolEvent schoolEvent = schoolEventRepository.findById(eventId).orElseThrow(
                () -> new BusinessLogicException(EventExceptionType.EVENT_NOT_FOUND));

        ResponseEventInfoDTO responseEventInfoDTO = new ResponseEventInfoDTO();
        responseEventInfoDTO.setEventId(eventId);
        responseEventInfoDTO.setEventName(schoolEvent.getEventName());
        responseEventInfoDTO.setEventPeriod(schoolEvent.getEventPeriod());
        responseEventInfoDTO.setCanceld(schoolEvent.isCanceled());
        responseEventInfoDTO.setModified(schoolEvent.isModified());

        return responseEventInfoDTO;
    }

    public List<ResponseEventInfoDTO> getEventInfoList(List<Long> eventIds){
        List<SchoolEvent> schoolEvents = schoolEventRepository.findAllByIds(eventIds);
        List<ResponseEventInfoDTO> responseEventInfoDTOList = new ArrayList<>();
        for (SchoolEvent schoolEvent : schoolEvents) {
            ResponseEventInfoDTO responseEventInfoDTO = new ResponseEventInfoDTO();
            responseEventInfoDTO.setEventId(schoolEvent.getId());
            responseEventInfoDTO.setEventName(schoolEvent.getEventName());
            responseEventInfoDTO.setEventPeriod(schoolEvent.getEventPeriod());
            responseEventInfoDTO.setCanceld(schoolEvent.isCanceled());
            responseEventInfoDTO.setModified(schoolEvent.isModified());
            responseEventInfoDTOList.add(responseEventInfoDTO);
        }
        return responseEventInfoDTOList;
    }
}
