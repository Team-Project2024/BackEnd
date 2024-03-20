package Hoseo.GraduationProject.Security.Redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private RedisTemplate redisTemplate;

    public RefreshTokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String save(final RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getMemberId());
        redisTemplate.expire(refreshToken.getRefreshToken(), 3, TimeUnit.DAYS); // 3일 후에는 자동으로 삭제되도록 수정
        return "Success";
    }

    public Optional<RefreshToken> findById(final String refreshToken) {
        System.out.println("find : " + refreshToken);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String memberId = valueOperations.get(refreshToken);

        if (Objects.isNull(memberId)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, memberId));
    }

    public boolean delete(final String refreshToken) {
        boolean a= redisTemplate.delete(refreshToken);
        return a;
    }
}
