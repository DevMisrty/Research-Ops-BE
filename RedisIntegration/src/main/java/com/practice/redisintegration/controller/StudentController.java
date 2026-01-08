package com.practice.redisintegration.controller;

import com.practice.redisintegration.model.Student;
import com.practice.redisintegration.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @Cacheable(cacheNames = "students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @Cacheable(cacheNames = "students", key = "#id")
    public Optional<Student> getStudentById(@PathVariable String id) {
        try{
            Thread.sleep(2000 );
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return studentService.getStudentById(id);
    }

    @PostMapping
    @CachePut(cacheNames = "students", key="#student.id")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    @CachePut(cacheNames = "students" , key = "#id")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student studentDetails) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDetails));
    }

    @DeleteMapping("/{id}")

    @CacheEvict(cacheNames = "students", key = "#id")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
