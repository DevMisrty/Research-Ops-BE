package com.practice.researchopsproject.utilities;

import org.jspecify.annotations.Nullable;

public class Messages {

     /* =========================
       AUTH / ACCESS
       ========================= */

    public static final String ACCESS_DENIED =
            "Access denied. You are not authorized to perform this operation.";

    public static final String RESEARCHER_ROLE_REQUIRED =
            "Only users with Researcher role are allowed to perform this action.";

    public static final String USER_NOT_AUTHENTICATED =
            "User is not authenticated.";

    /* =========================
       CASE RETRIEVAL
       ========================= */

    public static final String ASSIGNED_CASES_FETCH_SUCCESS =
            "Assigned cases fetched successfully.";

    public static final String ASSIGNED_CASE_FETCH_SUCCESS =
            "Assigned case details fetched successfully.";

    public static final String NO_ASSIGNED_CASES_FOUND =
            "No cases are assigned to the researcher.";

    public static final String CASE_NOT_ASSIGNED_TO_RESEARCHER =
            "The requested case is not assigned to the researcher.";

    public static final String CASE_NOT_FOUND =
            "Case.java not found.";

    /* =========================
       VALIDATION
       ========================= */

    public static final String INVALID_CASE_ID =
            "Invalid case identifier provided.";

    public static final String INVALID_RESEARCHER_ID =
            "Invalid researcher identifier provided.";

    /* =========================
       SYSTEM / ERROR
       ========================= */

    public static final String INTERNAL_SERVER_ERROR =
            "An unexpected error occurred while processing the request.";

    public static final String DATA_ACCESS_ERROR =
            "Failed to retrieve case data.";

    /* =========================
       LOGGING (Optional)
       ========================= */

    public static final String LOG_FETCH_ASSIGNED_CASES =
            "Fetching assigned cases for researcher.";

    public static final String LOG_FETCH_CASE_DETAILS =
            "Fetching assigned case details.";


    public static final String USER_NOT_FOUND = " No such user found ";
    public static final String USER_CREATED = "User has been created";
    public static final String LOGIN_SUCCESS = "Login Successful. ";
    public static final String INVALID_EXCEPTION = "Invalid Credentials";
    public static final String TOKEN_GENERATED = "New Tokens have been generated";
    public static final String LIST_OF_CASEMANAGER_FETCHED = "List of Case.java Manager has been fetched";
    public static final String LIST_OF_RESEARCHER_FETCHED = "List of Researcher has been fetched";
    public static final String MAIL_SEND = "Mail has been send to the email specified. ";
    public static final String TOKEN_EXPIRE = "Token has expire";
    public static final String CASEMANAGER_CREATED = "Case.java manager has successfully created. ";
    public static final String INVITATION_TOKEN_FETCHED = "Invitation token has been fetched successfully.";
    public static final String RESEARCHER_CREATED = "Researcher profile has successfully created. ";
    public static final String PROFILE_SET_NOACTIVE = "User profile set as Inactive  ";
    public static final String PROFILE_SET_ACTIVE = "User profile set as Active";
    public static final String LOGIN_FAILED = "Cant Log in, Profile is Inactive";
    public static final String CASE_CREATED = "new case has been created";
    public static final String CASE_UPDATED = "case has been updated";
    public static final String CASES_FETCHED_SUCCESSFULLY = "List of Cases fetched successfully";
    public static final String CASEMANAGER_NOT_FOUND = "Case.java Manager not found";
    public static final String RESEARCHER_NOT_FOUND = "Researcher not found. ";
    public static final String CASE_FETCHED_SUCCESSFULLY = "Case.java fetched Successfully. ";
    public static final String RESEARCHERPROFILE_FETCHED = "Researcher profile fetched successfully. ";
    public static final String CASEMANAGER_FETCHED = "Case.java manager profile fetched successfully. ";
    public static final String CANT_PERFROM_ON_ADMIN = "Cant perform such operation on admin user";
    public static final String FILE_EMPTY = "File cant be empty";
    public static final String CaseMANAGER_UPDATED = "CaseManager details has been updated.";
    public static final String RESEARCHER_UPDATED = "Researcher details has been updated.";
    public static final String UNAUTHORIZED_REQUEST = "U cant perform such operation.";
    public static final String CASEMANAGER_PROFILE_FETCHED = " CaseManager Details Fetched Successfully.";
    public static final String INVALID_TOKEN = "Token is Invalid";
    public static final String PASSWORD_DOESNT_MATCH = "Password and ConfirmPassword must match ";
    public static final String PASSWORD_UPDATED = "Password has been updated successfully.";

    //* ===================== Validation Messages ===================================*//

}
