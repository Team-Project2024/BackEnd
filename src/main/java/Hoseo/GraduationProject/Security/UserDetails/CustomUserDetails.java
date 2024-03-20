package Hoseo.GraduationProject.Security.UserDetails;

import Hoseo.GraduationProject.Member.Domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    @Getter
    private Member member;

    public CustomUserDetails(Member member){ this.member = member;}

    // 계정이 갖고있는 권한 목록을 리턴한다.  (권한이 여러개 있을수있어서 루프를 돌아야 하는데 우리는 한개만)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> {
            return member.getRole();
        }
        );

        return collectors;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    public String getId() {
        return member.getId();
    }

    public String getEmail() {
        return member.getEmail();
    }

    public String getMajor() {
        return member.getMajor();
    }

    @Override
    public String getUsername() {
        return member.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    /**
    * 이 부분에서 사용자가 사용 가능한 유저인지를 알아야되는데 우선은 true
     * 재학중인가? 교수가 아직 학교에서 일을 하는 중인가? 등을 알아야되는데 우리 DB에는 구분이 없음
    */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
