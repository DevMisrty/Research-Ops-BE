package com.spring.springsecuritypractice1.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/public/page")
    public String getPublicHomePage(){
        return "This is a Public Home Page";
    }

    @GetMapping("/page")
    public String getPage(){
        return "This is a Page1";
    }

    @GetMapping("/adminpage")
    public String getAdminPage(){
        return "This is an admin Page";
    }
}
