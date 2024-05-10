package Hoseo.GraduationProject.API.Event.Controller;

import Hoseo.GraduationProject.API.Event.DTO.Request.RequestEventListDTO;
import Hoseo.GraduationProject.API.Event.DTO.Request.RequestEventUpdateDTO;
import Hoseo.GraduationProject.API.Event.DTO.Response.ResponseEventInfoDTO;
import Hoseo.GraduationProject.API.Event.ExceptionType.EventExceptionType;
import Hoseo.GraduationProject.API.Event.Service.AdminEventService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/event")
public class AdminEventController {
    // role이 admin인 사용자만이 사용 가능한 API.
    // 교내 행사 추가, 취소, 날짜 변경 등
    private final AdminEventService adminEventService;

    // 모든 이벤트 조회
    // 조건을 걸어서 특정 이벤트만 조회하는게 나을라나?
    @GetMapping
    public ResponseEntity<List<ResponseEventInfoDTO>> getAllEvents() {
        List<ResponseEventInfoDTO> responseEventInfoDTOS = adminEventService.getAllEvents();
        if(responseEventInfoDTOS.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseEventInfoDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(adminEventService.getAllEvents());
    }

    // 이벤트 여러개 생성
    @PostMapping
    public ResponseEntity<Void> createEvent(@Valid @RequestBody RequestEventListDTO requestEventListDTO){
        adminEventService.createEvent(requestEventListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이벤트 취소 API
    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelEvent(@RequestParam(required = false) Long eventId){
        if(Objects.isNull(eventId))
            throw new BusinessLogicException(EventExceptionType.INVALID_INPUT_VALUE);
        adminEventService.cancelEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 이벤트 수정 API
    @PutMapping("/update")
    public ResponseEntity<Void> updateEvent(@Valid @RequestBody RequestEventUpdateDTO requestEventUpdateDTO){
        adminEventService.updateEvent(requestEventUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
