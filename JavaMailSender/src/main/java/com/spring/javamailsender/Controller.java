package com.spring.javamailsender;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final EmailSender emailSender;

    public Controller(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @GetMapping("/sendMail")
    public void sendMail(){
        emailSender.sendMail();
    }
}
