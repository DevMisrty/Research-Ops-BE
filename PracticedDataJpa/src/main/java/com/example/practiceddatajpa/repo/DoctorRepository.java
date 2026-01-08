package com.example.practiceddatajpa.repo;

import com.example.practiceddatajpa.model.Doctor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DoctorRepository extends CrudRepository<Doctor, UUID> {
}