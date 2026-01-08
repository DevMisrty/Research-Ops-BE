package com.HospitalManagement.practice.repository;

import com.HospitalManagement.practice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department,Long> {
}
