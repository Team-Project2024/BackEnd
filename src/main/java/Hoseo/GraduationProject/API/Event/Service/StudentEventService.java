package Hoseo.GraduationProject.API.Event.Service;

import Hoseo.GraduationProject.API.Event.DTO.Response.ResponseEventInfoDTO;
import Hoseo.GraduationProject.API.Event.Domain.SchoolEvent;
import Hoseo.GraduationProject.API.Event.ExceptionType.EventExceptionType;
import Hoseo.GraduationProject.API.Event.Repository.SchoolEventRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        responseEventInfoDTO.setCancled(schoolEvent.isCanceled());
        responseEventInfoDTO.setModified(schoolEvent.isModified());

        return responseEventInfoDTO;
    }
}
