package com.learning.expensetrackermdb.utility;

public class MessageConstants {
    // ==========================
    // Common
    // ==========================
    public static final String SUCCESS = "Operation completed successfully.";
    public static final String FAILED = "Operation failed.";
    public static final String INVALID_REQUEST = "Invalid request data.";

    // ==========================
    // User
    // ==========================
    public static final String USER_CREATED = "User registered successfully.";
    public static final String USER_ALREADY_EXISTS = "User with this email already exists.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String INVALID_CREDENTIALS = "Invalid email or password.";
    public static final String PASSWORD_NOT_MATCH = "Invalid email or password.";
    public static final String USER_DELETED = "User deleted successfully.";
    public static final String LOGIN_SUCCESS = "Login successful.";
    public static final String LOGOUT_SUCCESS = "Logout successful.";

    // ==========================
    // Authentication / JWT
    // ==========================
    public static final String TOKEN_GENERATED = "Token generated successfully.";
    public static final String TOKEN_INVALID = "Invalid or expired token.";
    public static final String ACCESS_DENIED = "You are not authorized to perform this action.";

    // ==========================
    // Expense
    // ==========================
    public static final String EXPENSE_CREATED = "Expense created successfully.";
    public static final String EXPENSE_UPDATED = "Expense updated successfully.";
    public static final String EXPENSE_DELETED = "Expense deleted successfully.";
    public static final String EXPENSE_NOT_FOUND = "Expense not found.";
    public static final String INVALID_EXPENSE_DATE = "Expense date cannot be in the future.";
    public static final String INVALID_EXPENSE_AMOUNT = "Expense amount must be greater than zero.";

    // ==========================
    // Budget
    // ==========================
    public static final String BUDGET_SET = "Budget set successfully.";
    public static final String BUDGET_UPDATED = "Budget updated successfully.";
    public static final String BUDGET_NOT_FOUND = "Budget not found.";
    public static final String BUDGET_EXCEEDED = "Budget limit exceeded.";
    public static final String BUDGET_EXISTS = "Budget already exists for this period.";

    // ==========================
    // Categories
    // ==========================
    public static final String CATEGORY_CREATED = "Category created successfully.";
    public static final String CATEGORY_NOT_FOUND = "Category not found.";
    public static final String CATEGORY_ALREADY_EXISTS = "Category already exists.";

    // ==========================
    // Logs
    // ==========================
    public static final String LOG_CREATED = "Log entry created.";

    // ==========================
    // Validation
    // ==========================
    public static final String MISSING_REQUIRED_FIELDS = "Required fields are missing.";
    public static final String INVALID_INPUT_FORMAT = "Invalid input format.";
    public static final String INVALID_ID = "Invalid ID format.";
    public static final String EXPENSE_FETCHED = "List of expenses fetched successfully.";

    public static String User_UPDATED = "User Data updated successfully.";
}
