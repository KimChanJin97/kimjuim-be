package com.cjkim.kimjuim.question;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.cjkim.kimjuim.question.QuestionExceptionInfo.*;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.my-email}")
    private String myEmail;
    private static final String SUBJECT_FORMAT = "[%s] ";
    private static final String BODY_FORMAT = """
            [ 이름 ]
            %s
            
            [ 이메일 ]
            %s
            
            [ 문의유형 ]
            %s
            
            [ 내용 ]
            %s
            
            [ 개인정보 수집 동의 ]
            %b
            """;

    public void sendSimpleMailMessage(
            String name,
            String email,
            String type,
            String title,
            String content,
            MultipartFile file,
            boolean agreement) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setReplyTo(email);
            helper.setTo(myEmail);
            helper.setSubject(String.format(SUBJECT_FORMAT, title));
            helper.setText(String.format(BODY_FORMAT, name, email, type, content, agreement), false);

            if (file != null && !file.isEmpty()) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new QuestionException(MESSAGE_FAILED);
        }
    }
}
