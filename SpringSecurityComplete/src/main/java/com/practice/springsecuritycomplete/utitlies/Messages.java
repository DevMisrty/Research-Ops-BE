package com.practice.springsecuritycomplete.utitlies;

public class Messages {

    /* =====================================================
           🔐 AUTHENTICATION MESSAGES
           ===================================================== */
    public static final String AUTH_LOGIN_SUCCESS = "Login successful";
    public static final String AUTH_LOGIN_FAILED = "Invalid username or password";
    public static final String AUTH_LOGOUT_SUCCESS = "Logout successful";
    public static final String AUTH_ACCOUNT_DISABLED = "Account is disabled";
    public static final String AUTH_ACCOUNT_LOCKED = "Account is locked";
    public static final String AUTH_ACCOUNT_EXPIRED = "Account has expired";
    public static final String AUTH_CREDENTIALS_EXPIRED = "Credentials have expired";
    public static final String AUTH_UNAUTHORIZED = "Unauthorized access";

    /* =====================================================
       🧾 JWT TOKEN MESSAGES
       ===================================================== */
    public static final String JWT_TOKEN_GENERATED = "JWT token generated successfully";
    public static final String JWT_TOKEN_VALID = "JWT token is valid";
    public static final String JWT_TOKEN_INVALID = "Invalid JWT token";
    public static final String JWT_TOKEN_EXPIRED = "JWT token has expired";
    public static final String JWT_TOKEN_MISSING = "JWT token is missing";
    public static final String JWT_TOKEN_MALFORMED = "Malformed JWT token";
    public static final String JWT_TOKEN_SIGNATURE_INVALID = "JWT token signature is invalid";
    public static final String JWT_REFRESH_TOKEN_GENERATED = "Refresh token generated successfully";
    public static final String JWT_REFRESH_TOKEN_EXPIRED = "Refresh token has expired";

    /* =====================================================
       🔑 AUTHORIZATION (ROLES)
       ===================================================== */
    public static final String ROLE_ACCESS_GRANTED = "Role-based access granted";
    public static final String ROLE_ACCESS_DENIED = "Access denied for the assigned role";
    public static final String ROLE_NOT_ASSIGNED = "Required role is not assigned to the user";
    public static final String ROLE_ADMIN_REQUIRED = "Admin role is required to access this resource";
    public static final String ROLE_USER_REQUIRED = "User role is required to access this resource";

    /* =====================================================
       🧩 PERMISSIONS (GRANULAR AUTHORIZATION)
       ===================================================== */
    public static final String PERMISSION_GRANTED = "Permission granted";
    public static final String PERMISSION_DENIED = "You do not have permission to perform this action";
    public static final String PERMISSION_MISSING = "Required permission is missing";
    public static final String PERMISSION_READ_REQUIRED = "Read permission is required";
    public static final String PERMISSION_WRITE_REQUIRED = "Write permission is required";
    public static final String PERMISSION_DELETE_REQUIRED = "Delete permission is required";
    public static final String PERMISSION_UPDATE_REQUIRED = "Update permission is required";

    /* =====================================================
       🌐 OAUTH2 AUTHENTICATION
       ===================================================== */
    public static final String OAUTH_LOGIN_SUCCESS = "OAuth2 login successful";
    public static final String OAUTH_LOGIN_FAILED = "OAuth2 authentication failed";
    public static final String OAUTH_PROVIDER_NOT_SUPPORTED = "OAuth2 provider is not supported";
    public static final String OAUTH_TOKEN_EXPIRED = "OAuth2 token has expired";
    public static final String OAUTH_EMAIL_NOT_FOUND = "Email not found from OAuth2 provider";

    /* =====================================================
       🚫 ACCESS CONTROL / EXCEPTIONS
       ===================================================== */
    public static final String ACCESS_DENIED = "You are not allowed to access this resource";
    public static final String FORBIDDEN = "Forbidden request";
    public static final String SECURITY_CONTEXT_MISSING = "Security context is missing";
    public static final String AUTHENTICATION_REQUIRED = "Authentication is required to access this resource";

    /* =====================================================
       📩 REQUEST / HEADER VALIDATION
       ===================================================== */
    public static final String AUTH_HEADER_MISSING = "Authorization header is missing";
    public static final String AUTH_HEADER_INVALID = "Invalid Authorization header format";
    public static final String BEARER_TOKEN_REQUIRED = "Bearer token is required";

    /* =====================================================
       🧪 GENERIC SECURITY ERRORS
       ===================================================== */
    public static final String SECURITY_ERROR = "Security error occurred";
    public static final String INTERNAL_SECURITY_ERROR = "Internal security processing error";
    public static final String USER_NOT_FOUND= "No such user found, pls register. ";

}
