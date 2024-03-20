package Hoseo.GraduationProject.Security.JWT;

import Hoseo.GraduationProject.Security.Redis.RefreshToken;
import Hoseo.GraduationProject.Security.Redis.RefreshTokenRepository;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //유저 정보
        //여기에서 억지로 getId하도록 설정하여 ID 가져옴
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("Access_Token", username, role, 600000L);
        String refresh = jwtUtil.createJwt("Refresh_Token", username, role, 86400000L);

        // 로그인에 성공하면 Redis에 키 - RefreshToken 값에 - 유저 ID를 넣어서 저장
        RefreshToken refreshToken = new RefreshToken(refresh, username);
        refreshTokenRepository.save(refreshToken);

        //응답 설정
        response.setHeader("Access_Token", access);
        response.addCookie(createCookie("Refresh_Token", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }


    //RefreshToken은 쿠키에 발급하기 때문에 쿠키를 만드는 메서드 추가
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //HTTPS 를 사용 할 경우에 true
        //cookie.setSecure(true);
        //쿠키가 적용될 범위
        //cookie.setPath("/");
        //JavaScript로 접근 불가능하게 막음
        cookie.setHttpOnly(true);

        return cookie;
    }
}
