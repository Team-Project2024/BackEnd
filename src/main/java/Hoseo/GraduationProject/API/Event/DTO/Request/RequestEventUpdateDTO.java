package Hoseo.GraduationProject.API.Event.DTO.Request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEventUpdateDTO {
    @NotNull
    private Long eventId;
    @NotBlank
    private String eventName;
    @NotBlank
    private String eventPeriod;
    //이벤트에 대한 설명은 null 가능
    @Nullable
    private String description;
}
