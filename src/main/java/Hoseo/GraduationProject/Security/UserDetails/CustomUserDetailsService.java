package Hoseo.GraduationProject.Security.UserDetails;

import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Member.Repository.MemberRepository;
import Hoseo.GraduationProject.Security.ExceptionType.SecurityExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회
        Member member = memberRepository.findById(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(SecurityExceptionType.NOT_FOUND.getErrorMessage());
                });
        return new CustomUserDetails(member);
    }
}
