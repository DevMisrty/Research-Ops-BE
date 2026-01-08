package com.practice.entityrelationshippract;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "http://localhost:5173" , "http://localhost:3000" })
public class Controller {

    @GetMapping("/getData")
    public String getData(){
        return " Data is fetched,";
    }

    @PutMapping("/saveData")
    public String saveData(@RequestBody Dto name){
        return "Data is Saved, "+ name;
    }

}
