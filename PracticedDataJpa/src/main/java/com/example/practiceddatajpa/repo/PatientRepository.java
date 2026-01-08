package com.example.practiceddatajpa.repo;

import com.example.practiceddatajpa.model.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PatientRepository extends CrudRepository<Patient, UUID> {
}