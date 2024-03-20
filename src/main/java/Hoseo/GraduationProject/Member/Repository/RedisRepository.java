package Hoseo.GraduationProject.Member.Repository;

import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.DTO.CertifiCode;
import Hoseo.GraduationProject.Member.DTO.VerificationCodeDTO;
import Hoseo.GraduationProject.Member.ExceptionType.MemberExceptionType;
import Hoseo.GraduationProject.Security.Redis.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate redisTemplate;

    public String save(String code, String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(code, id);
        redisTemplate.expire(code, 3, TimeUnit.MINUTES); // 3분 후에는 자동으로 삭제되도록 수정
        return "Success";
    }

    public void findByCode(VerificationCodeDTO verificationCodeDTO) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String memberId = valueOperations.get(verificationCodeDTO.getCode());

        if (Objects.isNull(memberId) || !memberId.equals(verificationCodeDTO.getId())) {
            throw new BusinessLogicException(MemberExceptionType.INVALID_CODE);
        }
    }

}
