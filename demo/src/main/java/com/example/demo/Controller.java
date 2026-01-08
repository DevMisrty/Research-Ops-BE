package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller {

    FormDataRepo formDataRepo;

    @PostMapping("/add")
    public FormData addFormData(@RequestBody FormData formData){
        return formDataRepo.save(formData);
    }
}
