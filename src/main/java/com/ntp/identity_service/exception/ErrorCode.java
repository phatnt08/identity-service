package com.ntp.identity_service.exception;

public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    USER_ALREADY_EXISTS(400, "User already exists"),
    USER_NOT_EXISTED(400, "User not existed"),
    USERNAME_INVALID(400, "Username must be at least 5 characters long"),
    PASSWORD_INVALID(400, "Password must be at least 8 characters long"),
    USER_NOT_FOUND(400, "User not found"),
    INVALID_CREDENTIALS(400, "Invalid credentials"),
    INVALID_TOKEN(400, "Invalid token")

    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
