package Hoseo.GraduationProject.Member.Controller;

import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.DTO.JoinDTO;
import Hoseo.GraduationProject.Member.ExceptionType.MemberExceptionType;
import Hoseo.GraduationProject.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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

}
