package Hoseo.GraduationProject.Security.JWT;

import Hoseo.GraduationProject.Security.Redis.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // Controller와 Service Layer 분리 ㄱㄱ
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("Refresh_Token")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token이 비어있습니다.", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getTokenType(refresh);

        if (!category.equals("Refresh_Token")) {

            //response status code
            return new ResponseEntity<>("refresh token이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        if(!refreshTokenRepository.findById(refresh).isPresent()){
            return new ResponseEntity<>("refresh token이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //이것도 createJWT를 Id를 받게 하고 수정 필요함
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("Access_Token", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("Refresh_Token", username, role, 86400000L);

        refreshTokenRepository.delete(refresh);

        //response 여기에서 우리는 Refresh_Token을 탈취를 방어하기 위해 Refresh Rotate를 사용 (재발급 요청마다 새로운 Refresh_Token을 전달해줌)
        response.setHeader("Access_Token", newAccess);
        response.addCookie(createCookie("Refresh_Token", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

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