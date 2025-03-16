package com.ntp.identity_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ALREADY_EXISTS(400, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(400, "User not existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(400, "Username must be at least {min} characters long", HttpStatus.BAD_REQUEST),
    USER_AGE_INVALID(400, "User's age must be at least {min} years old", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400, "Password must be at least {min} characters long", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(401, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "FORBIDDEN", HttpStatus.FORBIDDEN),

    PERMISSION_NOT_FOUND(404, "Permission not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(404, "Role not found", HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = statusCode;
    }

}
