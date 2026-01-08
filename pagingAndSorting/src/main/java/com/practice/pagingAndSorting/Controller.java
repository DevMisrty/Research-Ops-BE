package com.practice.pagingAndSorting;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class Controller {

    private final StudentRepo repo;


    @GetMapping("/student/firstName")
    public List<Student> getByFirstName(){
        return repo.findByOrderByFirstName();
    }

    @GetMapping("/student/lastName/desc")
    public List<Student> getByLastNameDesc(){
        return repo.findByOrderByLastNameDesc();
    }

    @GetMapping("/student/firstName/lastName")
    public List<Student> getByFirstNameAndLastName(){
        return repo.findAll(Sort.by("firstName").and(Sort.by("lastName")));
    }

    @GetMapping("/student/{page}")
    public List<Student> getStudentByPage(@PathVariable int page){
        Pageable pageable = PageRequest.of(page,4);
        return repo.findAll( pageable).getContent();
    }

    @GetMapping("/student")
    public List<Student> getStudentList(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int limit,
            @RequestParam(required = false, defaultValue = "ASC") String dir,
            @RequestParam(required = false, defaultValue = "id") String sortBy
    ){
        // creating and configuring the sort object.
        Sort sort = null;

        if("ASC".equals(dir)){
            sort = Sort.by(sortBy).ascending();
        }else if("DSC".equals(dir)){
            sort = Sort.by(sortBy).descending();
        }else{
            log.error("Bad Input value for dir, pls try again");
            return null;
        }

        Pageable pageable = PageRequest.of(page-1, limit, sort);
        return repo.findAll(pageable).getContent();
    }
}
