package Hoseo.GraduationProject.Member.Controller;

import Hoseo.GraduationProject.Member.DTO.*;
import Hoseo.GraduationProject.Member.DTO.Response.ResponseProfessorDTO;
import Hoseo.GraduationProject.Member.Service.MailSenderService;
import Hoseo.GraduationProject.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailSenderService mailSenderService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinProcess(
            @Valid @RequestBody JoinDTO joinDTO){
        memberService.joinProcess(joinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findId(
            @Valid @RequestBody FindUserIdDTO findUserIdDTO
            ){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findId(findUserIdDTO));
    }

    @PostMapping("/find-pw")
    public ResponseEntity<Void> findPw(
            @Valid @RequestBody FindUserPWDTO findUserPWDTO
    ) throws Exception {
        memberService.findUser(findUserPWDTO);
        mailSenderService.findPw(findUserPWDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/code-verification")
    public ResponseEntity<Void> CodeVerification(
            @Valid @RequestBody VerificationCodeDTO verificationCodeDTO
    ){
        memberService.codeVerification(verificationCodeDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePwDTO changePwDTO
    ){
        memberService.changePassword(changePwDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/admin/member-professor")
    public ResponseEntity<List<ResponseProfessorDTO>> getProfessorList(){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getProfessorList());
    }
}
