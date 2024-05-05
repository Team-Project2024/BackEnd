package Hoseo.GraduationProject.Member.Service;

import Hoseo.GraduationProject.Admin.Major.Service.MajorService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.DTO.*;
import Hoseo.GraduationProject.Member.DTO.Response.ResponseProfessorDTO;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Member.ExceptionType.MemberExceptionType;
import Hoseo.GraduationProject.Member.Repository.MemberRepository;
import Hoseo.GraduationProject.Member.Repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisRepository redisRepository;
    private final MajorService majorService;

    public void joinProcess(JoinDTO joinDTO){
        // 학번이 같은 회원이 있는지 확인
        Optional<Member> getMember = memberRepository.findById(joinDTO.getId());

        // 같은 학번인 회원이 없다면 진행
        if(getMember.isPresent()){
            // 같은 학번/교번으로 가입된 회원이 있을 경우 예외 처리
            throw new BusinessLogicException(MemberExceptionType.MEMBER_CONFLICT);
        } else{
            // ADMIN 계정은 서버를 통해 가입 불가능
            if(String.valueOf(joinDTO.getRole()).equals("ADMIN")){
                log.error("error log={}", MemberExceptionType.NOT_ACCESS_LEVEL.getErrorMessage());
                throw new BusinessLogicException(MemberExceptionType.NOT_ACCESS_LEVEL);
            } else{
                Member newMember = Member.builder()
                        .id(joinDTO.getId())
                        .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                        .name(joinDTO.getName())
                        .email(joinDTO.getEmail())
                        // ROLE_ + 권한으로 가입
                        .role("ROLE_"+String.valueOf(joinDTO.getRole()))
                        .teamwork(0L)
                        .entrepreneurship(0L)
                        .creativeThinking(0L)
                        .harnessingResource(0L)
                        .major(majorService.getMajor(joinDTO.getMajorId()))
                        .build();
                memberRepository.save(newMember);
            }
        }
    }

    public String findId(FindUserIdDTO findUserIdDTO){
        Member member = memberRepository.findByEmailAndName(findUserIdDTO.getEmail(),findUserIdDTO.getName());
        if(member == null) throw new BusinessLogicException(MemberExceptionType.NONE_MEMBER);
        return member.getId();
    }

    public void findUser(FindUserPWDTO findUserPWDTO){
        Member member = memberRepository.findByIdAndEmailAndName(findUserPWDTO.getId(), findUserPWDTO.getEmail(),findUserPWDTO.getName());
        if(member == null) throw new BusinessLogicException(MemberExceptionType.NONE_MEMBER);
    }

    public void codeVerification(VerificationCodeDTO verificationCodeDTO){
        redisRepository.findByCode(verificationCodeDTO);
    }

    public void changePassword(ChangePwDTO changePwDTO){
        if(changePwDTO.getPassword().equals(changePwDTO.getCheckPw())){
            Member member = memberRepository.findById(changePwDTO.getId()).orElseThrow(
                    () -> new BusinessLogicException(MemberExceptionType.NONE_MEMBER));
            try{
                member.updatePassword(bCryptPasswordEncoder.encode(changePwDTO.getPassword()));
                memberRepository.save(member);
            } catch(Exception e){
                throw new BusinessLogicException(MemberExceptionType.ERROR_CHANGE_PW);
            }
        }
    }

    // memberId로 멤버를 찾아서 반환하는 메서드
    public Member getMemberById(String memberId){
        return memberRepository.findById(memberId).orElseThrow(
                () -> new BusinessLogicException(MemberExceptionType.NONE_MEMBER));
    }

    // Role이 ROLE_PROFESSOR인 멤버들을 반환하는 메서드
    public List<ResponseProfessorDTO> getProfessorList(){
        List<Member> members = memberRepository.findByRoleProfessor();
        List<ResponseProfessorDTO> responseProfessorDTOS = new ArrayList<>();
        for(Member member: members){
            ResponseProfessorDTO responseProfessorDTO = new ResponseProfessorDTO();
            responseProfessorDTO.setId(member.getId());
            responseProfessorDTO.setName(member.getName());
            responseProfessorDTO.setDepartment(member.getMajor().getDepartment());

            responseProfessorDTOS.add(responseProfessorDTO);
        }
        return responseProfessorDTOS;
    }
}
