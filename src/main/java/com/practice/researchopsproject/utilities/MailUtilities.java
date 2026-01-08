package com.practice.researchopsproject.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
public class MailUtilities {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;


    @Async
    public void sendMail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

    }

}
