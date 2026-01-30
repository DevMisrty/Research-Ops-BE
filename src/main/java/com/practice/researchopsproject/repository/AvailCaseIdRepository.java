package com.practice.researchopsproject.repository;

import com.practice.researchopsproject.entity.AvailCaseId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AvailCaseIdRepository extends MongoRepository<AvailCaseId, String> {
    Optional<AvailCaseId> findFirstByOrderByCaseIdDesc();

    Optional<AvailCaseId> findFirstByOrderByCaseIdAsc();

    void deleteAvailCaseIdByCaseId(String caseId);
}
