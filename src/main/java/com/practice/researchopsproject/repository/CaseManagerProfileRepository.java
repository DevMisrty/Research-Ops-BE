package com.practice.researchopsproject.repository;

import com.practice.researchopsproject.entity.CaseManagerProfile;
import com.practice.researchopsproject.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CaseManagerProfileRepository extends MongoRepository<CaseManagerProfile, String> {

    CaseManagerProfile findByUser_Id(String userId);

    Optional<CaseManagerProfile> findByUser_Email(String userEmail);

    Optional<CaseManagerProfile> findByUser(Users users);

}
