package Hoseo.GraduationProject.API.Event.Controller;

import Hoseo.GraduationProject.API.Event.DTO.Response.ResponseEventInfoDTO;
import Hoseo.GraduationProject.API.Event.Service.StudentEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class StudentEventController {

    private final StudentEventService studentEventService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEventInfoDTO> getEventInfo(@PathVariable("id") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(studentEventService.getEventInfo(eventId));
    }
}
