package Hoseo.GraduationProject.Admin.Event.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEventUpdateDTO {
    private Long id;
    private String eventName;
    private String eventPeriod;
    private String description;
}
