package com.practice.researchopsproject.repository;

import com.practice.researchopsproject.entity.TempCase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TempCaseRepository extends MongoRepository<TempCase, String> {

    Optional<TempCase> findByCaseId(String caseId);

    Optional<TempCase> findFirstByIsCompleteFalseOrderByCreatedAtAsc();
}
