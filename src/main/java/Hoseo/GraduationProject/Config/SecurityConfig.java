package Hoseo.GraduationProject.Config;

import Hoseo.GraduationProject.Security.JWT.JWTFilter;
import Hoseo.GraduationProject.Security.JWT.JWTUtil;
import Hoseo.GraduationProject.Security.JWT.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    // 비밀번호 암호화를 위한 BCrypt
    // Bcrypt는 단방향 해시 알고리즘이다. 따라서 복호화가 불가능
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager를 Baen으로 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        // JWT는 세션은 stateless 상태로 관리해서 공격을 방어해주지 않아도 됨
        http
                .csrf((auth) -> auth.disable())

        //From 로그인 방식 disable
                .formLogin((auth) -> auth.disable())

        //http basic 인증 방식 disable
                .httpBasic((auth) -> auth.disable())

        //경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/student/**").hasRole("STUDENT") // STUDENT는 /api/student 패턴에만 접근 가능
                        .requestMatchers("/api/professor/**").hasRole("PROFESSOR") // PROFESSOR는 /api/professor 패턴에만 접근 가능
                        .requestMatchers("/api/admin/**", "/api/student/**", "/api/professor/**").hasRole("ADMIN") // ADMIN은 모든 패턴에 접근 가능
                        .anyRequest().permitAll()) //나머지는 모두에게 권한 있음

                // 로그인 이전에 세션 생성
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                // 필터 추가
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil)
                        , UsernamePasswordAuthenticationFilter.class)

        //세션 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
