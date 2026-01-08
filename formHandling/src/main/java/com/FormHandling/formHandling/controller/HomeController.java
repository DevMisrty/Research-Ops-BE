package com.FormHandling.formHandling.controller;

import com.FormHandling.formHandling.DTO.JobPostDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
public class HomeController {

    @RequestMapping(value = {"/" , "/home"})
    public String getHome(){
        System.out.println("calling from getHome() ");
        return "index.jsp";
    }

    @RequestMapping("/viewalljobs")
    public String getAllJobs(Model model){
        model.addAttribute("jobPosts", List.of(new JobPostDTO("1","Java","jxv sx",1,List.of("Java,JavaScript"))));
        return "viewalljobs.jsp";
    }
    @RequestMapping("/addjob")
    public String addNewJob(){
        return "addJob.jsp";
    }

    @RequestMapping("/handleForm")
    public String handlingForm(@ModelAttribute JobPostDTO jobPostDTO, Model model){
        log.info(String.valueOf(jobPostDTO));
        model.addAttribute("jobPost", jobPostDTO);
        return "success.jsp";
    }
}
