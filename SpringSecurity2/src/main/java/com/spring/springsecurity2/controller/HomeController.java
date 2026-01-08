package com.spring.springsecurity2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class    HomeController {

    @GetMapping("/home")
    public String getHome(){
        return "This is an open page";
    }

    @GetMapping("/customerhomepage")
    public String getCustomerHomePage(){
        return "This is Customer Home page";
    }

    @GetMapping("/adminpage")
    public String getAdminPage(){
        return "This is an Admin Page";
    }

    @GetMapping("/customerpage")
    public String getCustomerPage(){
        return "This is customer page, more restricted";
    }

}
