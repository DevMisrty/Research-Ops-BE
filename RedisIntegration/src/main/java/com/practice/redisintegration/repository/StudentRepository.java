package com.practice.redisintegration.repository;

import com.practice.redisintegration.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    // Custom query methods can be added here
}
