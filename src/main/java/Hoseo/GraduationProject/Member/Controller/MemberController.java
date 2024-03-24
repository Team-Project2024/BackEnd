package Hoseo.GraduationProject.Member.Controller;

import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.DTO.*;
import Hoseo.GraduationProject.Member.ExceptionType.MemberExceptionType;
import Hoseo.GraduationProject.Member.Service.MailSenderService;
import Hoseo.GraduationProject.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailSenderService mailSenderService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinProcess(
            @Valid @RequestBody JoinDTO joinDTO,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new BusinessLogicException(MemberExceptionType.MEMBER_SAVE_ERROR);
        }
        memberService.joinProcess(joinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/find-id")
    public ResponseEntity<String> findId(
            @Valid @RequestBody FindUserIdDTO findUserIdDTO
            ){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findId(findUserIdDTO));
    }

    @GetMapping("/find-pw")
    public ResponseEntity<Void> findPw(
            @Valid @RequestBody FindUserPWDTO findUserPWDTO
    ) throws Exception {
        memberService.findUser(findUserPWDTO);
        mailSenderService.findPw(findUserPWDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/code-verification")
    public ResponseEntity<Void> CodeVerification(
            @Valid @RequestBody VerificationCodeDTO verificationCodeDTO
    ){
        memberService.codeVerification(verificationCodeDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePwDTO changePwDTO
    ){
        memberService.changePassword(changePwDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
