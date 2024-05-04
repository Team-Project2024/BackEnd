package Hoseo.GraduationProject.Admin.Event.Controller;

import Hoseo.GraduationProject.Admin.Event.DTO.Request.RequestEventListDTO;
import Hoseo.GraduationProject.Admin.Event.DTO.Request.RequestEventUpdateDTO;
import Hoseo.GraduationProject.Admin.Event.DTO.Response.ResponseEventInfoDTO;
import Hoseo.GraduationProject.Admin.Event.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/event")
public class EventController {
    // role이 admin인 사용자만이 사용 가능한 API.
    // 교내 행사 추가, 취소, 날짜 변경 등
    private final EventService eventService;

    // 모든 이벤트 조회
    // 조건을 걸어서 특정 이벤트만 조회하는게 나을라나?
    @GetMapping
    public ResponseEntity<List<ResponseEventInfoDTO>> getAllEvents() {
        List<ResponseEventInfoDTO> responseEventInfoDTOS = eventService.getAllEvents();
        if(responseEventInfoDTOS.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseEventInfoDTOS);
        }
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvents());
    }

    // 이벤트 여러개 생성
    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody RequestEventListDTO requestEventListDTO){
        eventService.createEvent(requestEventListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이벤트 취소 API
    @PutMapping("/cancle")
    public ResponseEntity<Void> cancleEvent(@RequestParam Long eventId){
        eventService.cancleEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 이벤트 수정 API
    @PutMapping("/update")
    public ResponseEntity<Void> updateEvent(@RequestBody RequestEventUpdateDTO requestEventUpdateDTO){
        eventService.updateEvent(requestEventUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
