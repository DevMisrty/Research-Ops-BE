package com.HospitalManagement.practice.repository;

import com.HospitalManagement.practice.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctor,Long> {
}
