package com.example.practiceddatajpa.repo;

import com.example.practiceddatajpa.model.Insurance;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface InsuranceRepository extends CrudRepository<Insurance, UUID> {
}