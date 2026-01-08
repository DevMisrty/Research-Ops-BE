package com.spring.javamailsender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendMail(){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(sender);
        mail.setTo("dev.mistry.unused@gmail.com");
        mail.setText("""
                This is for verification purpose, pls note down the following code,
                
                252525255
                """);
        mail.setSubject("For 2- Step verification purpose. ");
        mailSender.send(mail);
        log.info("Mail has been send successfully..... ");
    }
}
