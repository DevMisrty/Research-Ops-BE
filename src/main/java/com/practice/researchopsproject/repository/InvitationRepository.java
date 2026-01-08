package com.practice.researchopsproject.repository;

import com.practice.researchopsproject.entity.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface InvitationRepository extends MongoRepository<Invitation, UUID> {
}
