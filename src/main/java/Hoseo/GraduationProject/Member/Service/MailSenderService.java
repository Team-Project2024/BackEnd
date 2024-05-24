package Hoseo.GraduationProject.Member.Service;

import Hoseo.GraduationProject.Member.DTO.FindUserPWDTO;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Member.Repository.RedisRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final RedisRepository redisRepository;
    private final MemberService memberService;

    @Async("threadPoolTaskExecutor")
    public void findPw(FindUserPWDTO findUserPWDTO) throws Exception {
        Member member = memberService.getMemberById(findUserPWDTO.getId());
        log.info("Thread: {}", Thread.currentThread().getName());
        Random random = new Random();

        // 0부터 999999 사이의 숫자를 생성
        int randomNumber = random.nextInt(1000000);

        // 생성된 숫자를 6자리로 맞추기 위해 문자열로 변환
        String sixDigitNumber;

        while(true){
            sixDigitNumber = String.format("%06d", randomNumber);
            if(redisRepository.save(sixDigitNumber, findUserPWDTO.getId())){
                break;
            }
            log.warn("warn log={}", sixDigitNumber); // 얼마나 자주 값이 겹치는지 확인
        }

        Context context = new Context();
        context.setVariable("name", member.getName());
        context.setVariable("data", sixDigitNumber);

        String message = templateEngine.process("CertifiNumber.html", context);

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, false, "UTF-8"); // 2번째 인자는 Multipart여부 결정
        mimeMessageHelper.setFrom("gwangjeg14@gmail.com"); //누가
        mimeMessageHelper.setTo(findUserPWDTO.getEmail()); //누구에게?
        mimeMessageHelper.setSubject("LUMOS 비밀번호 변경 인증번호"); //제목
        mimeMessageHelper.setText(message, true);

        try {
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("error log={}", "메일 전송 실패");
        }
    }
}
