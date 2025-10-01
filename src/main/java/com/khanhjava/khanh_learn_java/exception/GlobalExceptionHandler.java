package com.khanhjava.khanh_learn_java.exception;

import com.khanhjava.khanh_learn_java.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(
            AppException ex,
            HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        return ApiResponse.error(errorCode.getMessage(),
                                 errorCode.getCode(),
                                 errorCode.getStatusCode(),
                                 request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String enumKey = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.KEY_VALID;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constrainViolation = ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constrainViolation.getConstraintDescriptor().getAttributes();
        } catch (IllegalArgumentException iex) {
            // LOG exception
        }
        return ApiResponse.error(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(),
                                                                            attributes
                                 ) : errorCode.getMessage(),
                                 errorCode.getCode(),
                                 errorCode.getStatusCode(),
                                 request.getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ApiResponse.error(errorCode.getMessage(),
                                 errorCode.getCode(),
                                 errorCode.getStatusCode(),
                                 request.getRequestURI()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        return ApiResponse.error(errorCode.getMessage(),
                                 errorCode.getCode(),
                                 errorCode.getStatusCode(),
                                 request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        return ApiResponse.error(ex.getMessage(), 9999, HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    // Function
    private String mapAttribute(String message, Map<String, Object> attributes) {
        String MIN_ATTRIBUTE = "min";
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
