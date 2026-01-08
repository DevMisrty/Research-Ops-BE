package com.learning.expensetrackermdb.repository;

import com.learning.expensetrackermdb.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsersRepo extends  MongoRepository<Users, String> {
    boolean existsUsersByEmail(String email);

    void deleteUsersByEmail(String email);

    Optional<Users> findByEmail(String email);
}
