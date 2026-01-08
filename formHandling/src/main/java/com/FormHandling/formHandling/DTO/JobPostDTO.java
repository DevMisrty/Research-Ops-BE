package com.FormHandling.formHandling.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ToString
public class JobPostDTO {

    String postId;
    String postProfile;
    String postDesc;
    Integer reqExperience;
    List<String> postTechStack;

}
