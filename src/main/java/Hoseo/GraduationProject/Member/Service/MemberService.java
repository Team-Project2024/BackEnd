package Hoseo.GraduationProject.Member.Service;

import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.DTO.JoinDTO;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Member.ExceptionType.MemberExceptionType;
import Hoseo.GraduationProject.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
                throw new BusinessLogicException(MemberExceptionType.NOT_ACCESS_LEVEL);
            } else{
                Member newMember = Member.builder()
                        .id(joinDTO.getId())
                        .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                        .name(joinDTO.getName())
                        .email(joinDTO.getEmail())
                        .major(joinDTO.getMajor())
                        // ROLE_ + 권한으로 가입
                        .role("ROLE_"+String.valueOf(joinDTO.getRole()))
                        .build();
                memberRepository.save(newMember);
            }
        }
    }
}
