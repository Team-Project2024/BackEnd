package Hoseo.GraduationProject.API.Event.DTO.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEventInfoDTO {
    private Long eventId;
    private String eventName;
    private String eventPeriod;
    private String description;
    private boolean isCancled;
    private boolean modified;
}
