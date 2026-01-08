package com.learning.Mongodb.controller;

import com.learning.Mongodb.documents.Student;
import com.learning.Mongodb.dto.StudentDto;
import com.learning.Mongodb.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class StudentController {

    private StudentRepository studentRepository;

    @PostMapping("/student")
    public Student createStudent(@RequestBody StudentDto studentDto){
        Student student = Student.builder()
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .phoneNumber(studentDto.getPhoneNumber())
                .date(LocalDateTime.now())
                .build();
        return studentRepository.save(student);
    }

    @GetMapping("/students")
    public List<Student>  getAllStudents(){
        return studentRepository.findAll();
    }

    @GetMapping("/students/{firstname}")
    public Student getStudentByFirstName(@PathVariable String firstname){
        return studentRepository.findByFirstName(firstname);
    }

    @GetMapping("/allstudents/{firstname}")
    public List<Student> getAllStudentsByFirstName(@PathVariable String firstname){
        return studentRepository.findAllByFirstName(firstname);
    }
}
