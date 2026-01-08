package com.spring.springsecuritypractice1.repository;

import com.spring.springsecuritypractice1.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends CrudRepository<Users,Long> {

    Optional<Users> findByUsername(String username);
}
