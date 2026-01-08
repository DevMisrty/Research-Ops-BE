package com.WeatherEmailService.Project.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void SendMail(String to, String subject, NewApiData data){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(data.toString());
        javaMailSender.send(mail);
    }

    public void SendMail(String to, String subject, ArrayList<NewApiData.Article> data){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(data.toString());
        javaMailSender.send(mail);
    }

}
