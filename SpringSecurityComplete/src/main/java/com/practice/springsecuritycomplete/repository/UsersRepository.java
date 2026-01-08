package com.practice.springsecuritycomplete.repository;

import com.practice.springsecuritycomplete.entity.Users;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends ListCrudRepository<Users,String> {
    Optional<Users> findUsersByEmail(String email);
}
