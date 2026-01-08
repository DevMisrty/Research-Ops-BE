package com.learning.expensetrackermdb.repository;

import com.learning.expensetrackermdb.entity.Expense;
import com.learning.expensetrackermdb.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public interface ExpenseRepo extends MongoRepository<Expense, String> {
    List<Users> findByUsers(Users users);

    List<Expense> findByUsers_Id(String id);

    List<Expense> findByCategoryAndUsers_Id(String category,String id);

    List<Expense> findByUsers_IdAndDateBetween(String usersId, LocalDateTime dateAfter, LocalDateTime dateBefore);
}
