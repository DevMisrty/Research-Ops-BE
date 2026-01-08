package com.WeatherEmailService.Project.EmailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class NewApi {

    private static final String api = "https://newsapi.org/v2/top-headlines?country=us&apiKey=231b28fdccc34f8f80c58a0c1d97533f";
    private static  String searchApi ="https://newsapi.org/v2/everything?q=SEARCH&apiKey=231b28fdccc34f8f80c58a0c1d97533f";


    private static EmailService emailService;
    private static RestTemplate restTemplate;

    public NewApi(EmailService emailService, RestTemplate restTemplate) {
        this.emailService=emailService;
        this.restTemplate= restTemplate;
    }

    public NewApiData getNews(){
        ResponseEntity<NewApiData> response = restTemplate.exchange(api, HttpMethod.GET, null, NewApiData.class);
        NewApiData data = response.getBody();
        log.info("..........");
        emailService.SendMail("dmistry11011@gmail.com","Todays Top Highlighted News",data);
        return data;
    }

    public NewApiData getSearchedInfo(String search){
        String finalApi = searchApi.replace("SEARCH",search);
        ResponseEntity<NewApiData> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, NewApiData.class);
        NewApiData body = response.getBody();
        return body;
    }


}
