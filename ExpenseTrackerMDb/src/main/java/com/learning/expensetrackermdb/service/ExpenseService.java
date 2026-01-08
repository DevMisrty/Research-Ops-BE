package com.learning.expensetrackermdb.service;

import com.learning.expensetrackermdb.dto.request.ExpenseRequestDto;
import com.learning.expensetrackermdb.dto.response.ExpenseResponseDto;
import com.learning.expensetrackermdb.exception.customexception.UserNotFoundException;

import java.util.List;

public interface ExpenseService {
    ExpenseResponseDto addExpense(ExpenseRequestDto expenseRequestDto, String email) throws UserNotFoundException;

    List<ExpenseResponseDto> getExpense(String email) throws UserNotFoundException;

    List<ExpenseResponseDto> getExpenseByCategory(String category, String email) throws UserNotFoundException;

    List<ExpenseResponseDto> getExpenseByMonth(String email) throws UserNotFoundException;
}
