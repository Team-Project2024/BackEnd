package Hoseo.GraduationProject.Member.Service;

import Hoseo.GraduationProject.Member.DTO.FindUserPWDTO;
import Hoseo.GraduationProject.Member.Repository.RedisRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final RedisRepository redisRepository;

    @Async
    public void findPw(FindUserPWDTO findUserPWDTO) throws Exception {
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
        }

        Context context = new Context();
        context.setVariable("message", "인증번호는");
        context.setVariable("data", sixDigitNumber);

        String message = templateEngine.process("CertifiNumber.html", context);

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, false, "UTF-8"); // 2번째 인자는 Multipart여부 결정
        mimeMessageHelper.setFrom("gwangjeg14@gmail.com"); //누가
        mimeMessageHelper.setTo(findUserPWDTO.getEmail()); //누구에게?
        mimeMessageHelper.setSubject("인증번호 발송"); //제목
        mimeMessageHelper.setText(message, true);

        try {
            javaMailSender.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
