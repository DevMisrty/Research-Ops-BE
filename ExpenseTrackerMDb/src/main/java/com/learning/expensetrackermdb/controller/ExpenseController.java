package com.learning.expensetrackermdb.controller;

import com.learning.expensetrackermdb.dto.request.ExpenseRequestDto;
import com.learning.expensetrackermdb.dto.response.ExpenseResponseDto;
import com.learning.expensetrackermdb.exception.customexception.UserNotFoundException;
import com.learning.expensetrackermdb.jwt.JWTUtility;
import com.learning.expensetrackermdb.service.ExpenseService;
import com.learning.expensetrackermdb.service.UsersService;
import com.learning.expensetrackermdb.utility.ApiResponse;
import com.learning.expensetrackermdb.utility.MessageConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UsersService usersService;
    private final JWTUtility utility;

    @PostMapping("/add-expense")
    public ResponseEntity<?> addExpense(@RequestBody ExpenseRequestDto expenseRequestDto, HttpServletRequest request) throws UserNotFoundException {

        String email = utility.getEmailFromToken(request.getHeader("Authorization").substring(7));
        if(!usersService.checkUserExists(email)){
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        }

        ExpenseResponseDto response = expenseService.addExpense(expenseRequestDto, email);
        return ApiResponse.generateApiResponse(HttpStatus.CREATED, MessageConstants.EXPENSE_CREATED, response);
    }

    @GetMapping("/get-expense")
    public ResponseEntity<?> getExpense(HttpServletRequest request) throws UserNotFoundException {
        String email = utility.getEmailFromToken(request.getHeader("Authorization").substring(7));
        if(!usersService.checkUserExists(email)){
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        }
        return ApiResponse.generateApiResponse(HttpStatus.OK, MessageConstants.EXPENSE_FETCHED, expenseService.getExpense(email));
    }

    @GetMapping("/get-expense/{category}")
    public ResponseEntity<?> getExpenseByCategory(@PathVariable String category, HttpServletRequest request) throws UserNotFoundException {
        String email = utility.getEmailFromToken(request.getHeader("Authorization").substring(7));
        if(!usersService.checkUserExists(email)){
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        }
        return ApiResponse.
                generateApiResponse(HttpStatus.OK, MessageConstants.EXPENSE_FETCHED, expenseService.getExpenseByCategory(category.toUpperCase(), email));
    }

    @GetMapping("/get-expense/month")
    public ResponseEntity<?> getExpenseByMonth(HttpServletRequest request) throws UserNotFoundException {
        String email = utility.getEmailFromToken(request.getHeader("Authorization").substring(7));
        if(!usersService.checkUserExists(email)){
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        }
        return ApiResponse.generateApiResponse(HttpStatus.OK, MessageConstants.EXPENSE_FETCHED, expenseService.getExpenseByMonth(email));
    }


}
