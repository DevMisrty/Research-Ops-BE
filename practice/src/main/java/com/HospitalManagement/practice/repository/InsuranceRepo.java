package com.HospitalManagement.practice.repository;

import com.HospitalManagement.practice.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepo extends JpaRepository<Insurance,Long> {
}
