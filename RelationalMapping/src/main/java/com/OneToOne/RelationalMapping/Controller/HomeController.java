package com.OneToOne.RelationalMapping.Controller;

import com.OneToOne.RelationalMapping.Model.Message;
import com.OneToOne.RelationalMapping.Repo.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HomeController {

    MessageRepo messageRepo;

    @PostMapping("/add")
    public Message addMessage(@RequestBody Message message){
        return messageRepo.save(message);
    }
}
