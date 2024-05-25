package Hoseo.GraduationProject.Security.JWT;

import Hoseo.GraduationProject.Exception.ErrorResponse;
import Hoseo.GraduationProject.Exception.ExceptionType;
import Hoseo.GraduationProject.Member.DTO.LoginRequest;
import Hoseo.GraduationProject.Security.ExceptionType.SecurityExceptionType;
import Hoseo.GraduationProject.Security.Redis.RefreshToken;
import Hoseo.GraduationProject.Security.Redis.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    private final ObjectMapper objectMapper;

    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 클라이언트에서 전송한 JSON 데이터를 DTO 클래스로 변환
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            // DTO 클래스에서 필요한 정보 추출
            String username = loginRequest.getId();
            String password = loginRequest.getPassword();

            if (!username.matches("^(\\d{6}|\\d{8})$")) {
                throw new DisabledException(SecurityExceptionType.ID_ERROR.getErrorMessage());
            }
            if (!password.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")) {
                throw new DisabledException(SecurityExceptionType.PASSWORD_ERROR.getErrorMessage());
            }

            // UsernamePasswordAuthenticationToken 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
            // AuthenticationManager를 통해 인증 시도
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new DisabledException(SecurityExceptionType.FAIL_LOGIN.getErrorMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        //유저 정보
        //여기에서 억지로 getId하도록 설정하여 ID 가져옴
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("Access_Token", username, role, 1800000L); // 30분
        String refresh = jwtUtil.createJwt("Refresh_Token", username, role, 21600000L); //6시간

        // 로그인에 성공하면 Redis에 키 - RefreshToken 값에 - 유저 ID를 넣어서 저장
        RefreshToken refreshToken = new RefreshToken(refresh, username);
        refreshTokenRepository.save(refreshToken);

        //응답 설정
        response.setContentType("application/json");

        response.addCookie(createCookie("Refresh_Token", refresh));
        response.setStatus(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Access_Token", access);
        String responseBodyJson = objectMapper.writeValueAsString(responseBody);

        PrintWriter writer = response.getWriter();
        writer.println(responseBodyJson);
        writer.flush();
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error(failed.getMessage());
        if(failed instanceof UsernameNotFoundException){
            setErrorResponse(response, SecurityExceptionType.NOT_FOUND, failed.getMessage());
        }else if(failed instanceof BadCredentialsException) {
            setErrorResponse(response, SecurityExceptionType.BAD_CREDENTIALS, failed.getMessage());
        }else if(failed instanceof DisabledException){
            setErrorResponse(response, SecurityExceptionType.FAIL_LOGIN, failed.getMessage());
        }
    }

    private void setErrorResponse(HttpServletResponse response, ExceptionType exceptionType,String message) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(exceptionType.getErrorCode());
        ErrorResponse errorBody = ErrorResponse.of(exceptionType.getErrorCode(),message);
        new ObjectMapper().writeValue(response.getWriter(), errorBody);
    }


    //RefreshToken은 쿠키에 발급하기 때문에 쿠키를 만드는 메서드 추가
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(6*60*60); // 6시간동안 쿠키에 유지
        //HTTPS 를 사용 할 경우에 true
        //cookie.setSecure(true);
        //쿠키가 적용될 범위
        //cookie.setPath("/");
        //JavaScript로 접근 불가능하게 막음
        cookie.setSecure(true);
        cookie.setAttribute("SameSite","None");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
