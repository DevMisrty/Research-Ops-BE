package com.practice.redisintegration.service;

import com.practice.redisintegration.model.Student;
import com.practice.redisintegration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(String id, Student studentDetails) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(studentDetails.getName());
                    student.setEmail(studentDetails.getEmail());
                    student.setDepartment(studentDetails.getDepartment());
                    student.setAge(studentDetails.getAge());
                    return studentRepository.save(student);
                })
                .orElseGet(() -> {
                    studentDetails.setId(id);
                    return studentRepository.save(studentDetails);
                });
    }

    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }
}
