package com.HospitalManagement.practice.repository;

import com.HospitalManagement.practice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<Patient,Long> {
}
