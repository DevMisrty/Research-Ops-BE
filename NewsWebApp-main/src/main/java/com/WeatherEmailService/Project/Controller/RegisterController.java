package com.WeatherEmailService.Project.Controller;

import com.WeatherEmailService.Project.EmailService.NewApi;
import com.WeatherEmailService.Project.EmailService.NewApiData;
import com.WeatherEmailService.Project.Entity.User;
import com.WeatherEmailService.Project.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewApi newApi;

    @RequestMapping("/")
    public String login(){
        return "Register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user ){
        userRepository.save(user);
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        ArrayList<NewApiData.Article> articles =newApi.getNews().getArticles();
        model.addAttribute("news",articles);
        return "home";
    }

    @RequestMapping("/search")
    public String searchHome(@RequestParam("searchValue") String searchValue,Model model){
        if(searchValue == null || searchValue.isEmpty()){
            model.addAttribute("message", "No results found for your search");
            return "home";
        }
        ArrayList<NewApiData.Article> searchedInfo = newApi.getSearchedInfo(searchValue).getArticles();
        List<NewApiData.Article> collect = searchedInfo.stream().filter(x -> x.getAuthor() != null).collect(Collectors.toList());
        model.addAttribute("news",collect);
        log.info("***********");
        return "home";
    }

}
