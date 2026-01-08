package com.learning.Mongodb.repository;


import com.learning.Mongodb.documents.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, ObjectId>{

    List<Student> findAllByFirstName(String firstName);
    Student findByFirstName(String firstName);
}
