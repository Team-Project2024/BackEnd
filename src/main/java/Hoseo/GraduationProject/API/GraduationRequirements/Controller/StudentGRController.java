package Hoseo.GraduationProject.API.GraduationRequirements.Controller;

import Hoseo.GraduationProject.API.GraduationRequirements.DTO.GRDTO;
import Hoseo.GraduationProject.API.GraduationRequirements.Service.StudentGRService;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/graduation")
public class StudentGRController {
    private final StudentGRService studentGRService;

    @GetMapping
    public ResponseEntity<GRDTO> getGR(@AuthenticationPrincipal CustomUserDetails member){
        return ResponseEntity.status(HttpStatus.OK).body(studentGRService.getGR(member.getMember().getId()));
    }
}
