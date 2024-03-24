package Hoseo.GraduationProject.Member.Repository;

import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.DTO.VerificationCodeDTO;
import Hoseo.GraduationProject.Member.ExceptionType.MemberExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate redisTemplate;

    public boolean save(String code, String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 만약 해당 코드가 이미 Redis에 존재한다면 저장하지 않고 실패를 반환
        if (valueOperations.get(code) != null) {
            return false;
        }

        valueOperations.set(code, id);
        redisTemplate.expire(code, 6, TimeUnit.HOURS); // 3분 후에는 자동으로 삭제되도록 수정
        return true;
    }

    public void findByCode(VerificationCodeDTO verificationCodeDTO) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String memberId = valueOperations.get(verificationCodeDTO.getCode());

        if (Objects.isNull(memberId) || !memberId.equals(verificationCodeDTO.getId())) {
            log.error("error log={}", MemberExceptionType.INVALID_CODE.getErrorMessage());
            throw new BusinessLogicException(MemberExceptionType.INVALID_CODE);
        }
    }

}
