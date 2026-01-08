package com.WeatherEmailService.Project.EmailService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;



@Getter
@Setter
@ToString
public class NewApiData{
    private String status;
    private int totalResults;
    private ArrayList<Article> articles;

    @Getter @Setter
    @ToString
    public static class Article{
        public String author;
        public String title;
        public String description;
        public String url;
        public Date publishedAt;
    }
}



