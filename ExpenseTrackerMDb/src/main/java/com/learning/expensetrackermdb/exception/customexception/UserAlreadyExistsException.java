package com.learning.expensetrackermdb.exception.customexception;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String userAlreadyExists) {
        super(userAlreadyExists);
    }
}
