package com.WeatherEmailService.Project.Service;

import com.WeatherEmailService.Project.EmailService.EmailService;
import com.WeatherEmailService.Project.EmailService.NewApi;
import com.WeatherEmailService.Project.EmailService.NewApiData;
import com.WeatherEmailService.Project.Entity.User;
import com.WeatherEmailService.Project.Repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SchedulingEmail {

    private UserRepository userRepository;
    private EmailService emailService;
    private NewApi newApi;

    public SchedulingEmail(UserRepository userRepository, EmailService emailService,NewApi newApi) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.newApi = newApi;
    }

    @Scheduled(cron="0 0 7 * * *")
    public void Scheduledtask(){
        List<User> all = userRepository.findAll();
        if(all.size()!=0) {
            ArrayList<NewApiData.Article> articles = newApi.getNews().getArticles();
            for (User user : all) {
                emailService.SendMail(user.getEmail(), "Todays Highlines", articles);
            }
        }
    }
}
