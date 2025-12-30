package kr.co.kosmo.project_back.mail.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    // 보내는 사람
    private static final String SENDER = "hyunee7155@naver.com";
    // 인증번호 메일 발송
    public void sendAuthCode(String toEmail, String authCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(SENDER);
            message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
            message.setSubject("[이메일 인증] 인증번호 안내");
            message.setText(
                "인증번호는 다음과 같습니다.\n\n" +
                authCode +
                "\n\n1분 내에 입력해주세요");

            javaMailSender.send(message);
        } catch(Exception e) {
            throw new RuntimeException("인증 메일 전송 실패", e);
        }
    }
}
// 실제 메일을 보내는 역할(메일만 보냄. 인증 흐름/세션 관여 X)