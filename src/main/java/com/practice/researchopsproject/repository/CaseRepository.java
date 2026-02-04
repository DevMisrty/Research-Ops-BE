package com.practice.researchopsproject.repository;

import com.practice.researchopsproject.entity.Case;
import com.practice.researchopsproject.entity.CaseManagerProfile;
import com.practice.researchopsproject.entity.PracticeArea;
import com.practice.researchopsproject.entity.ResearcherProfile;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CaseRepository extends MongoRepository<Case, String> {
    Optional<Case> findByCaseId(String caseId);

    Page<Case> findByIsValidTrueAndCaseNameContainingOrIsValidTrueAndClientNameContainingOrIsValidTrueAndPracticeAreaContaining(String searchBy, String searchBy1, String searchBy2, PageRequest pageRequest);

    Page<Case> findAllByIsValidTrue(PageRequest pageRequest);

    Page<Case> findAllByCreatorAndIsValidTrue(CaseManagerProfile caseManager, PageRequest pageRequest);

    Page<Case> findAllByCreatorAndCaseNameContainingIgnoreCaseAndIsValidTrueOrCreatorAndClientNameContainingIgnoreCaseAndIsValidTrueOrCreatorAndPracticeAreaAndIsValidTrue(CaseManagerProfile caseManager, String searchBy, CaseManagerProfile caseManager1, String searchBy1, CaseManagerProfile caseManager2, String searchBy2, PageRequest pageRequest);

    Page<Case> findAllByResearchersAndIsValidTrue(ResearcherProfile profile, PageRequest pageRequest);

    @Query("""
        {
          'isValid': true,
          'researchers.$id': ?0,
          $or: [
            { 'caseName': { $regex: ?1, $options: 'i' } },
            { 'clientName': { $regex: ?1, $options: 'i' } },
            { 'practiceArea': ?1 }
          ]
        }
    """)
    Page<Case> searchByResearcherAndKeyword(
            ObjectId researcherId,
            String searchBy,
            Pageable pageable
    );

    Optional<Case> findFirstByIsValidFalseAndCreatedOnBeforeOrderByCaseIdAsc(LocalDateTime localDateTime);
}
