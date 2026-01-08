package com.practice.researchopsproject.repository;

import com.practice.researchopsproject.entity.ResearcherProfile;
import com.practice.researchopsproject.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResearcherProfileRepository extends MongoRepository<ResearcherProfile, String> {
    Optional<ResearcherProfile> findByUser_Id(String userId);


    Optional<ResearcherProfile> findByUser_Email(String userEmail);

    Page<ResearcherProfile> findAllByUser_Active(boolean b, PageRequest pageRequest);

    Optional<ResearcherProfile> findByUser(Users researcherUser);

    Page<ResearcherProfile> findAllByUser_ActiveTrue(PageRequest pageRequest);
}
