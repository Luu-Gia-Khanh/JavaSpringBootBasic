package com.khanhjava.khanh_learn_java.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXIST(1001, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "Unauthorized", HttpStatus.FORBIDDEN),

    // FOR KEY VALIDATION
    KEY_VALID(1000, "Invalid key", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_VALID(1002, "Username must be at least 3 characters long", HttpStatus.BAD_REQUEST),
    PASSWORD_VALID(1003, "Password must be at least 6 characters long", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
