package com.learning.expensetrackermdb.service.implementation;

import java.time.LocalDate;
import com.learning.expensetrackermdb.dto.request.ExpenseRequestDto;
import com.learning.expensetrackermdb.dto.response.ExpenseResponseDto;
import com.learning.expensetrackermdb.entity.Expense;
import com.learning.expensetrackermdb.entity.Users;
import com.learning.expensetrackermdb.exception.customexception.UserNotFoundException;
import com.learning.expensetrackermdb.repository.ExpenseRepo;
import com.learning.expensetrackermdb.repository.UsersRepo;
import com.learning.expensetrackermdb.service.ExpenseService;
import com.learning.expensetrackermdb.utility.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepo expenseRepo;
    private final ModelMapper modelMapper;
    private final UsersRepo usersRepo;


    @Override
    public ExpenseResponseDto addExpense(ExpenseRequestDto expenseRequestDto, String email) throws UserNotFoundException {
        Users users = usersRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        Expense expense = modelMapper.map(expenseRequestDto, Expense.class);
        expense.setUsers(users);
        expense.setCategory(expense.getCategory().toUpperCase());
        expense.setDate(LocalDateTime.now());
        expenseRepo.save(expense);
        return modelMapper.map(expense, ExpenseResponseDto.class);

    }

    @Override
    public List<ExpenseResponseDto> getExpense(String email) throws UserNotFoundException {
        Users users = usersRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        List<Expense> expenses = expenseRepo.findByUsers_Id(users.getId());
        return expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseResponseDto.class))
                .toList();
    }

    @Override
    public List<ExpenseResponseDto> getExpenseByCategory(String category, String email) throws UserNotFoundException {
        Users users = usersRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        List<Expense> expenses = expenseRepo.findByCategoryAndUsers_Id(category , users.getId());
        return expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseResponseDto.class))
                .toList();
    }

    @Override
    public List<ExpenseResponseDto> getExpenseByMonth(String email) throws UserNotFoundException {
        Users users = usersRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);


        List<Expense> expenses = expenseRepo.findByUsers_IdAndDateBetween(users.getId(), startOfMonth, endOfMonth);
        return expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseResponseDto.class))
                .toList();
    }
}
