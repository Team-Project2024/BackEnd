package Hoseo.GraduationProject.Admin.Event.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEventDTO {
    private String eventName;
    private String eventPeriod;
    private String description;
}
