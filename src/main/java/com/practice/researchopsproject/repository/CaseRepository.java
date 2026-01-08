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

import java.util.List;
import java.util.Optional;

public interface CaseRepository extends MongoRepository<Case, String> {
    Optional<Case> findByCaseId(String caseId);

    Page<Case> findAllByCreator(CaseManagerProfile caseManager, PageRequest pageRequest);

    Page<Case> findAllByResearchers(ResearcherProfile profile, PageRequest pageRequest);

    Page<Case> findAllByCaseNameContainingOrClientNameContainingOrPracticeAreaContaining(String searchBy, String searchBy1, String searchBy2, PageRequest pageRequest);

    Page<Case> findAllByCreatorAndCaseNameContainingIgnoreCaseOrCreatorAndClientNameContainingIgnoreCaseOrCreatorAndPracticeArea(CaseManagerProfile caseManagerProfile, String searchBy, CaseManagerProfile caseManagerProfile1, String searchBy1, CaseManagerProfile caseManagerProfile2, String searchBy2, PageRequest pageRequest);


    Page<Case> findAllByResearchersAndCaseNameContainingIgnoreCaseOrResearchersAndClientNameContainsIgnoreCaseAndResearchersOrPracticeAreaContaining(ResearcherProfile profile, String searchBy, ResearcherProfile profile1, String searchBy1, ResearcherProfile profile2, String searchBy2, PageRequest pageRequest);

    Page<Case> findAllByResearchersAndCaseNameContainingIgnoreCaseOrResearchersAndClientNameContainsIgnoreCaseOrResearchersOrPracticeAreaContaining(ResearcherProfile profile, String searchBy, ResearcherProfile profile1, String searchBy1, ResearcherProfile profile2, String searchBy2, PageRequest pageRequest);
}
