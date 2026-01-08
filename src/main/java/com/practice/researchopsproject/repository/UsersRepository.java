package com.practice.researchopsproject.repository;

import com.practice.researchopsproject.entity.Role;
import com.practice.researchopsproject.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UsersRepository extends MongoRepository<Users, String> {
    Optional<Users> findByEmail(String email);

    Page<Users> findAllByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String search, PageRequest pageRequest);

    Page<Users> findAllByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndRole(String name, Role role, PageRequest pageRequest);

    Page<Users> findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(Role role, String name, Role role1, String email, Pageable pageable);

    Page<Users> findAllByIsActiveAndRole(boolean isActive, Role role, Pageable pageable);
}
