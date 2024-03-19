package Hoseo.GraduationProject.Member.Controller;

import Hoseo.GraduationProject.Member.DTO.JoinDTO;
import Hoseo.GraduationProject.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinProcess(JoinDTO joinDTO){
        memberService.joinProcess(joinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
