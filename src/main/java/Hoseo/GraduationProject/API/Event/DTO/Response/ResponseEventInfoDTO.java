package Hoseo.GraduationProject.API.Event.DTO.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEventInfoDTO {
    private Long eventId;
    private String eventName;
    private String eventPeriod;
    private String description;
    private boolean isCanceld;
    private boolean modified;

    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
