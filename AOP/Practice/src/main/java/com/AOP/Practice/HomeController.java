package com.AOP.Practice;

import com.AOP.Practice.DTO.ContactDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @RequestMapping(value = {"/", "/home"})
    public String home(Model model) {
        model.addAttribute("contact",new ContactDTO());
        return "form";
    }

    @PostMapping("/contact")
    public String contact(@Valid @ModelAttribute ContactDTO contact , Errors error){
        if(error.hasErrors()){
            System.out.println("In valid input " + error.getAllErrors().toString());
            return "redirect:/";
        }
        System.out.println(contact);
        return "redirect:/";
    }
}