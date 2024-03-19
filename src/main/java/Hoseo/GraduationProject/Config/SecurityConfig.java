package Hoseo.GraduationProject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 비밀번호 암호화를 위한 BCrypt
    // Bcrypt는 단방향 해시 알고리즘이다. 따라서 복호화가 불가능
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
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
                        .requestMatchers("/api/student").hasRole("STUDENT")
                        .requestMatchers("/api/professor").hasRole("PROFESSOR")
                        .requestMatchers("/api/admin").hasRole("ADMIN")
                        .anyRequest().permitAll()) //나머지는 모두에게 권한 있음

        //세션 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
