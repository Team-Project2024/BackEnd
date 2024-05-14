package Hoseo.GraduationProject.Chat.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDjangoDTO {
    private String content;
    private String table;
    private List<Long> data;
}
